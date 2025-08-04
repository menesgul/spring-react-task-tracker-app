import React, { createContext, useContext, useReducer, useEffect } from "react";
import { useAuth } from "./context/AuthContext.tsx";
import apiClient from "./api";
import TaskList from "./domain/TaskList";
import Task from "./domain/Task";
import { TaskPriority } from "./domain/TaskPriority.ts";
import { TaskStatus } from "./domain/TaskStatus.ts";

interface AppState {
  taskLists: TaskList[];
  tasks: { [taskListId: string]: Task[] };
}

type Action =
    | { type: "FETCH_TASKLISTS"; payload: TaskList[] }
    | { type: "GET_TASKLIST"; payload: TaskList }
    | { type: "CREATE_TASKLIST"; payload: TaskList }
    | { type: "UPDATE_TASKLIST"; payload: TaskList }
    | { type: "DELETE_TASKLIST"; payload: string }
    | { type: "FETCH_TASKS"; payload: { taskListId: string; tasks: Task[] } }
    | { type: "CREATE_TASK"; payload: { taskListId: string; task: Task } }
    | { type: "GET_TASK"; payload: { taskListId: string; task: Task } }
    | {
  type: "UPDATE_TASK";
  payload: { taskListId: string; taskId: string; task: Task };
}
    | { type: "DELETE_TASK"; payload: { taskListId: string; taskId: string } };

// Action types
const FETCH_TASKLISTS = "FETCH_TASKLISTS";
const GET_TASKLIST = "GET_TASKLIST";
const CREATE_TASKLIST = "CREATE_TASKLIST";
const UPDATE_TASKLIST = "UPDATE_TASKLIST";
const DELETE_TASKLIST = "DELETE_TASKLIST";
const FETCH_TASKS = "FETCH_TASKS";
const CREATE_TASK = "CREATE_TASK";
const GET_TASK = "GET_TASK";
const UPDATE_TASK = "UPDATE_TASK";
const DELETE_TASK = "DELETE_TASK";

// Reducer
const reducer = (state: AppState, action: Action): AppState => {
  switch (action.type) {
    case FETCH_TASKLISTS:
      return { ...state, taskLists: action.payload };
    case GET_TASKLIST:
      return {
        ...state,
        taskLists: state.taskLists.some((wl) => wl.id === action.payload.id)
            ? state.taskLists.map((wl) =>
                wl.id === action.payload.id ? action.payload : wl
            )
            : [...state.taskLists, action.payload],
      };
    case CREATE_TASKLIST:
      return { ...state, taskLists: [...state.taskLists, action.payload] };
    case UPDATE_TASKLIST:
      return {
        ...state,
        taskLists: state.taskLists.map((wl) =>
            wl.id === action.payload.id ? action.payload : wl
        ),
      };
    case DELETE_TASKLIST:
      return {
        ...state,
        taskLists: state.taskLists.filter((wl) => wl.id !== action.payload),
      };
    case FETCH_TASKS:
      return {
        ...state,
        tasks: {
          ...state.tasks,
          [action.payload.taskListId]: action.payload.tasks,
        },
      };
    case CREATE_TASK:
      return {
        ...state,
        tasks: {
          ...state.tasks,
          [action.payload.taskListId]: [
            ...(state.tasks[action.payload.taskListId] || []),
            action.payload.task,
          ],
        },
      };
    case GET_TASK: {
      // Get existing tasks or initialize empty array
      const existingTasks = state.tasks[action.payload.taskListId] || [];

      // Check if task exists
      const taskExists = existingTasks.some(
          (task) => task.id === action.payload.task.id
      );

      // Either update existing task or add new one
      const updatedTasks = taskExists
          ? existingTasks.map((task) =>
              task.id === action.payload.task.id ? action.payload.task : task
          )
          : [...existingTasks, action.payload.task];

      return {
        ...state,
        tasks: {
          ...state.tasks,
          [action.payload.taskListId]: updatedTasks,
        },
      };
    }
    case UPDATE_TASK:
      return {
        ...state,
        tasks: {
          ...state.tasks,
          [action.payload.taskListId]: state.tasks[
              action.payload.taskListId
              ].map((task) =>
              task.id === action.payload.taskId ? action.payload.task : task
          ),
        },
      };
    case DELETE_TASK:
      return {
        ...state,
        tasks: {
          ...state.tasks,
          [action.payload.taskListId]: state.tasks[
              action.payload.taskListId
              ].filter((task) => task.id !== action.payload.taskId),
        },
      };
    default:
      return state;
  }
};

// Initial state
const initialState: AppState = {
  taskLists: [],
  tasks: {},
};

// Context
interface AppContextType {
  state: AppState;
  api: {
    fetchTaskLists: () => Promise<void>;
    getTaskList: (id: string) => Promise<void>;
    createTaskList: (taskList: Omit<TaskList, "id">) => Promise<void>;
    updateTaskList: (id: string, taskList: TaskList) => Promise<void>;
    deleteTaskList: (id: string) => Promise<void>;
    fetchTasks: (taskListId: string) => Promise<void>;
    createTask: (
        taskListId: string,
        task: {
          reminder: {
            date: string | undefined | null;
            isDateEnabled: boolean;
            repeatType: string;
            time: string | null;
            isTimeEnabled: boolean
          } | null;
          dueDate: Date | undefined;
          description: string;
          title: string;
          priority: TaskPriority;
          status: undefined
        }
    ) => Promise<void>;
    getTask: (taskListId: string, taskId: string) => Promise<void>;
    updateTask: (
        taskListId: string,
        taskId: string,
        task: {
          reminder: {
            date: string | undefined | null;
            isDateEnabled: boolean;
            repeatType: string;
            time: string | null;
            isTimeEnabled: boolean
          } | null;
          dueDate: Date | undefined;
          description: string;
          id: string;
          title: string;
          priority: TaskPriority;
          status: TaskStatus | undefined
        }
    ) => Promise<void>;
    deleteTask: (taskListId: string, taskId: string) => Promise<void>;
  };
}

const AppContext = createContext<AppContextType | undefined>(undefined);

// Provider component
export const AppProvider: React.FC<{ children: React.ReactNode }> = ({
                                                                       children,
                                                                     }) => {
  const [state, dispatch] = useReducer(reducer, initialState);
  const { token } = useAuth();



  // API calls - Using apiClient instead of api
  const api: AppContextType["api"] = {
    fetchTaskLists: async () => {
      try {
        const response = await apiClient.get<TaskList[]>("/task-lists");
        dispatch({ type: FETCH_TASKLISTS, payload: response.data });
      } catch (error) {
        console.error("Error fetching task lists:", error);
        throw error;
      }
    },

    getTaskList: async (id: string) => {
      try {
        const response = await apiClient.get<TaskList>(`/task-lists/${id}`);
        dispatch({ type: GET_TASKLIST, payload: response.data });
      } catch (error) {
        console.error("Error getting task list:", error);
        throw error;
      }
    },

    createTaskList: async (taskList) => {
      try {
        const response = await apiClient.post<TaskList>("/task-lists", taskList,);
        dispatch({ type: CREATE_TASKLIST, payload: response.data });
      } catch (error) {
        console.error("Error creating task list:", error);
        throw error;
      }
    },

    updateTaskList: async (id, taskList) => {
      try {
        const response = await apiClient.put<TaskList>(`/task-lists/${id}`, taskList,);
        dispatch({ type: UPDATE_TASKLIST, payload: response.data });
      } catch (error) {
        console.error("Error updating task list:", error);
        throw error;
      }
    },

    deleteTaskList: async (id) => {
      try {
        await apiClient.delete(`/task-lists/${id}`,);
        dispatch({ type: DELETE_TASKLIST, payload: id });
      } catch (error) {
        console.error("Error deleting task list:", error);
        throw error;
      }
    },

    fetchTasks: async (taskListId) => {
      try {
        const response = await apiClient.get<Task[]>(`/task-lists/${taskListId}/tasks`,);
        dispatch({
          type: FETCH_TASKS,
          payload: { taskListId, tasks: response.data },
        });
      } catch (error) {
        console.error("Error fetching tasks:", error);
        throw error;
      }
    },

    createTask: async (taskListId, task) => {
      try {
        const response = await apiClient.post<Task>(`/task-lists/${taskListId}/tasks`, task,);
        dispatch({
          type: CREATE_TASK,
          payload: { taskListId, task: response.data },
        });
      } catch (error) {
        console.error("Error creating task:", error);
        throw error;
      }
    },

    getTask: async (taskListId: string, taskId: string) => {
      try {
        const response = await apiClient.get<Task>(`/task-lists/${taskListId}/tasks/${taskId}`,);
        dispatch({
          type: GET_TASK,
          payload: { taskListId, task: response.data },
        });
      } catch (error) {
        console.error("Error getting task:", error);
        throw error;
      }
    },

    updateTask: async (taskListId, taskId, task) => {
      try {
        const response = await apiClient.put<Task>(`/task-lists/${taskListId}/tasks/${taskId}`, task,);
        dispatch({
          type: UPDATE_TASK,
          payload: { taskListId, taskId, task: response.data },
        });
      } catch (error) {
        console.error("Error updating task:", error);
        throw error;
      }
    },

    deleteTask: async (taskListId, taskId) => {
      try {
        await apiClient.delete(`/task-lists/${taskListId}/tasks/${taskId}`,);
        dispatch({ type: DELETE_TASK, payload: { taskListId, taskId } });
      } catch (error) {
        console.error("Error deleting task:", error);
        throw error;
      }
    },
  };

  useEffect(() => { if (token) api.fetchTaskLists() }, [token])

  return (
      <AppContext.Provider value={{ state, api }}>{children}</AppContext.Provider>
  );
};

// Custom hook to use the context
export const useAppContext = (): AppContextType => {
  const context = useContext(AppContext);
  if (context === undefined) {
    throw new Error("useAppContext must be used within an AppProvider");
  }
  return context;
};