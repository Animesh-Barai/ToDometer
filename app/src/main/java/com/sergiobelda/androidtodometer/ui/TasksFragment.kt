/*
 * Copyright 2020 Sergio Belda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sergiobelda.androidtodometer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.sergiobelda.androidtodometer.R
import com.sergiobelda.androidtodometer.databinding.TasksFragmentBinding
import com.sergiobelda.androidtodometer.model.Task
import com.sergiobelda.androidtodometer.ui.adapter.TasksAdapter
import com.sergiobelda.androidtodometer.ui.swipe.SwipeController
import com.sergiobelda.androidtodometer.util.MaterialDialog
import com.sergiobelda.androidtodometer.util.MaterialDialog.Companion.icon
import com.sergiobelda.androidtodometer.util.MaterialDialog.Companion.message
import com.sergiobelda.androidtodometer.util.MaterialDialog.Companion.negativeButton
import com.sergiobelda.androidtodometer.util.MaterialDialog.Companion.positiveButton
import com.sergiobelda.androidtodometer.viewmodel.MainViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * [Fragment] showing the list of tasks.
 */
class TasksFragment : Fragment() {
    private var _binding: TasksFragmentBinding? = null
    private val binding get() = _binding!!

    private val tasksAdapter: TasksAdapter = TasksAdapter()

    private val mainViewModel by sharedViewModel<MainViewModel>()
    // NOTE: using Koin we can write also:
    // private val mainViewModel by lazy { getViewModel<MainViewModel>() }
    // Using fragment-ktx extension:
    // private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TasksFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.tasksRecyclerView.adapter = tasksAdapter
        mainViewModel.projectTaskListingList.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                binding.emptyListImage.visibility = View.VISIBLE
                binding.emptyListMessage.visibility = View.VISIBLE
            } else {
                binding.emptyListImage.visibility = View.GONE
                binding.emptyListMessage.visibility = View.GONE
            }
            tasksAdapter.submitList(it)
        })
        tasksAdapter.taskClickListener = object : TasksAdapter.TaskClickListener {
            override fun onTaskClick(task: Task, view: View) {
                val extras = FragmentNavigatorExtras(
                    view to task.taskId.toString()
                )
                val action = TasksFragmentDirections.navToTask(taskId = task.taskId)
                findNavController().navigate(action, extras)
            }

            override fun onDeleteTaskClick(task: Task) {}

            override fun onTaskDoneClick(task: Task) {
                mainViewModel.setTaskDone(task.taskId)
            }

            override fun onTaskDoingClick(task: Task) {
                mainViewModel.setTaskDoing(task.taskId)
            }
        }

        setSwipeActions()
    }

    private fun setSwipeActions() {
        val swipeController = SwipeController(requireContext(), object :
            SwipeController.SwipeControllerActions {
            override fun onDelete(position: Int) {
                MaterialDialog.createDialog(requireContext()) {
                    icon(R.drawable.ic_warning_24dp)
                    message(R.string.delete_task_dialog)
                    positiveButton("Ok") {
                        tasksAdapter.currentList?.get(position)?.task?.taskId?.let { taskId ->
                            mainViewModel.deleteTask(
                                taskId
                            )
                        }
                    }
                    negativeButton("Cancel")
                }.show()
            }
        })
        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(binding.tasksRecyclerView)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val TAG = "TasksFragment"
    }
}
