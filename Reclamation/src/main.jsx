import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './index.css'
import {createBrowserRouter , RouterProvider} from 'react-router-dom';
import Login from './Login.jsx';

const router = createBrowserRouter([
  {
    //default to try new features
    path:'/',
    element: <Login/>
  },
  {
    path: '/home',
    element: <App/>
  },
  {
    path: '/Login',
    element:<Login/>
  }
]);

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <RouterProvider router={router}/>
  </React.StrictMode>,
)
