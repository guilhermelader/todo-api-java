<template>
  <div class="container mx-auto p-4">
    <h1 class="text-2xl font-bold mb-4">Task List</h1>
    <button @click="openTaskModal('add')" class="bg-green-500 text-white px-4 py-2 rounded-md mb-4">Add Task</button>
    <table class="min-w-full bg-white border text-center">
      <thead>
        <tr>
          <th class="py-2 px-4 border-b">Title</th>
          <th class="py-2 px-4 border-b">Description</th>
          <th class="py-2 px-4 border-b">Deadline</th>
          <th class="py-2 px-4 border-b">Completed</th>
          <th class="py-2 px-4 border-b">Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="task in tasks" :key="task.id" :class="{'bg-red-100': task.isOverdue}">
          <td class="py-2 px-4 border-b">{{ task.title }}</td>
          <td class="py-2 px-4 border-b">{{ task.description }}</td>
          <td class="py-2 px-4 border-b">{{ formatDate(task.deadline) }}</td>
          <td class="py-2 px-4 border-b">
            <input type="checkbox" :checked="task.completed" disabled>
          </td>
          <td class="py-2 px-4 border-b">
            <div class="flex flex-col space-y-2">
              <button @click="openTaskModal('edit', task)" class="bg-yellow-500 text-white px-4 py-2 rounded-md">Edit</button>
              <button @click="deleteTask(task.id)" class="bg-red-500 text-white px-4 py-2 rounded-md">Delete</button>
            </div>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-if="isTaskModalOpen" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex justify-center items-center">
      <div class="bg-white p-6 rounded-md shadow-md w-2/3 h-2/4">
        <h2 class="text-xl font-bold mb-4">{{ isEditMode ? 'Edit Task' : 'Add Task' }}</h2>
        <form @submit.prevent="isEditMode ? updateTask() : submitNewTask()">
          <div class="mb-4">
            <label class="block text-gray-700">Deadline</label>
            <input v-model="taskForm.deadline" type="text" placeholder="dd-MM-yyyy" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm" required mas>
          </div>
          <div class="mb-4">
            <label class="block text-gray-700">Title</label>
            <input v-model="taskForm.title" type="text" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm" required>
          </div>
          <div class="mb-4">
            <label class="block text-gray-700">Description</label>
            <textarea v-model="taskForm.description" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm" required></textarea>
          </div>
          <div class="mb-4">
            <label class="block text-gray-700">Completed</label>
            <input v-model="taskForm.completed" type="checkbox" class="mt-1 block">
          </div>
          <div class="flex justify-end">
            <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded-md">{{ isEditMode ? 'Update Task' : 'Create Task' }}</button>
            <button @click="closeTaskModal" type="button" class="ml-2 bg-gray-500 text-white px-4 py-2 rounded-md">Cancel</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import moment from 'moment-timezone';

export default {
  data() {
    return {
      taskForm: {
        title: '',
        description: '',
        deadline: '',
        completed: false,
      },
      currentTask: null,
      tasks: [],
      isTaskModalOpen: false,
      isEditMode: false,
    };
  },
  methods: {
    formatDateToISO(dateStr) {
      const parsedDate = moment.utc(dateStr, 'DD-MM-YYYY', true);
      if (!parsedDate.isValid()) {
        console.error('Invalid date when converting to ISO:', dateStr);
        return '';
      }
      return parsedDate.startOf('day').toISOString();
    },
    formatDate(dateStr) {
      const parsedDate = new Date(dateStr);
      if (isNaN(parsedDate)) {
        console.error('Invalid date when formatting for display:', dateStr);
        return '';
      }
      return parsedDate.toLocaleString('pt-BR', { day: '2-digit', month: '2-digit', year: 'numeric' });
    },
    openTaskModal(mode, task = null) {
      if (mode === 'edit') {
        if (task && task.id) {
          this.currentTask = task;
          this.taskForm = {
            ...task,
            deadline: task.deadline ? this.formatDate(task.deadline) : ''
          };
          this.isEditMode = true;
        } else {
          console.error('No task provided for edit');
          return;
        }
      } else {
        this.taskForm = {
          title: '',
          description: '',
          deadline: '',
          completed: false,
        };
        this.isEditMode = false;
      }
      this.isTaskModalOpen = true;
    },
    closeTaskModal() {
      this.isTaskModalOpen = false;
      this.currentTask = null;
    },
    submitNewTask() {
      const formattedDeadline = this.formatDateToISO(this.taskForm.deadline);

      if (!moment(formattedDeadline).isValid()) {
        alert('Invalid date, please insert date in format dd-mm-yyy');
        return;
      }

      if (moment(formattedDeadline).isBefore(moment().startOf('day'))) {
        alert('Date must be in present or future');
        return;
      }

      const newTaskWithFormattedDeadline = {
        ...this.taskForm,
        deadline: formattedDeadline
      };

      axios.post('http://localhost:8080/api/tasks', newTaskWithFormattedDeadline)
        .then(response => {
          console.log(response);
          this.fetchTasks();
          this.closeTaskModal();
        })
        .catch(error => {
          console.error('Error adding task:', error);
        });
    },
    updateTask() {
      if (!this.currentTask || !this.currentTask.id) {
        console.error('No task selected for update');
        return;
      }

      const formattedDeadline = this.formatDateToISO(this.taskForm.deadline);

      if (!moment(formattedDeadline).isValid()) {
        alert('Invalid date, please insert date in format dd-mm-yyy');
        return;
      }

      if (moment(formattedDeadline).isBefore(moment().startOf('day'))) {
        alert('Date must be in present or future');
        return;
      }

      const updatedTask = {
        ...this.taskForm,
        deadline: formattedDeadline
      };

      axios.put(`http://localhost:8080/api/tasks/${this.currentTask.id}`, updatedTask)
        .then(response => {
          console.log(response);
          this.fetchTasks();
          this.closeTaskModal();
        })
        .catch(error => {
          console.error('Error updating task:', error);
        });
    },
    deleteTask(taskId) {
      axios.delete(`http://localhost:8080/api/tasks/${taskId}`)
        .then(response => {
          console.log(response);
          this.fetchTasks();
        })
        .catch(error => {
          console.error('Error deleting task:', error);
        });
    },
    fetchTasks() {
      axios.get('http://localhost:8080/api/tasks')
        .then(response => {
          this.tasks = response.data.content.map(task => ({
            ...task,
            deadline: this.formatDate(task.deadline)
          }));
        })
        .catch(error => {
          console.error('Error fetching tasks:', error);
        });
    }
  },
  mounted() {
    this.fetchTasks();
  },
};
</script>

<style scoped>
.bg-red-100 {
  background-color: #fee2e2;
}

.text-center th,
.text-center td {
  text-align: center;
}
</style>
