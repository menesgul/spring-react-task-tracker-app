import "./App.css";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "./context/AuthContext";
import PrivateLayout from "./components/PrivateLayout";
import TaskLists from "./components/TaskListsScreen";
import CreateUpdateTaskListScreen from "./components/CreateUpdateTaskListScreen";
import TaskListScreen from "./components/TasksScreen";
import CreateUpdateTaskScreen from "./components/CreateUpdateTaskScreen";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";

// PrivateRoute: token yoksa /login'e yönlendirir
function PrivateRoute({ children }: { children: JSX.Element }) {
  const { token } = useAuth();
  return token ? children : <Navigate to="/login" replace />;
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Auth-free rota */}
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        {/* Korunmalı rotalar */}
        <Route
          path="/"
          element={
            <PrivateRoute>
              <PrivateLayout>
                <TaskLists />
              </PrivateLayout>
            </PrivateRoute>
          }
        />
        <Route
          path="/new-task-list"
          element={
            <PrivateRoute>
              <PrivateLayout>
                <CreateUpdateTaskListScreen />
              </PrivateLayout>
            </PrivateRoute>
          }
        />
        <Route
          path="/edit-task-list/:listId"
          element={
            <PrivateRoute>
              <PrivateLayout>
                <CreateUpdateTaskListScreen />
              </PrivateLayout>
            </PrivateRoute>
          }
        />
        <Route
          path="/task-lists/:listId"
          element={
            <PrivateRoute>
              <PrivateLayout>
                <TaskListScreen />
              </PrivateLayout>
            </PrivateRoute>
          }
        />
        <Route
          path="/task-lists/:listId/new-task"
          element={
            <PrivateRoute>
              <PrivateLayout>
                <CreateUpdateTaskScreen />
              </PrivateLayout>
            </PrivateRoute>
          }
        />
        <Route
          path="/task-lists/:listId/edit-task/:taskId"
          element={
            <PrivateRoute>
              <PrivateLayout>
                <CreateUpdateTaskScreen />
              </PrivateLayout>
            </PrivateRoute>
          }
        />
        {/* Bilinmeyen rotalarda anasayfaya yönlendir */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
}
