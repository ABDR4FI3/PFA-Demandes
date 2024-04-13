import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";



function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigateTo = useNavigate();

  const checkTokenValidity = async () => {
    try {
      const token = localStorage.getItem('token');
  
      // Check if token exists
      if (!token) {
        throw new Error('Token not found in localStorage');
      }
  
      const response = await fetch(`http://localhost:9090/checkToken?token=${token}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
  
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
  
      const data = await response.json();
      console.log('Token validity response:', data);
  
      // Delete token from local storage after using it
      localStorage.removeItem('token');
  
      return data; // This will be a boolean value
    } catch (error) {
      console.error('There was a problem checking token validity:', error);
      return null;
    }
  };
    
  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:9090/users/authenticate", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      });

      if (!response.ok) {
        throw new Error("Invalid credentials");
      }

      const data = await response.json();
      localStorage.setItem("token", data.token);
      setError("");

      navigateTo("/home");
    } catch (error) {
      setError(error.message);
    }
  };

    // Check if user is already authenticated (token present in localStorage)
    useEffect(() => {
      const token = localStorage.getItem("token");
      if (token) {
        const valid = checkTokenValidity(token)
        if(valid){
          navigateTo("/home");
        }else{
          localStorage.removeItem("tokem")
          navigateTo("/Login")
        }

      }
    }, [navigateTo]);
  return (
    <div className="bg-slate-800 w-screen h-screen flex justify-center items-center">
      <form className="max-w-sm mx-auto border-2 p-10 w-full" onSubmit={handleSubmit}>
        <div className="mb-5">
          <label htmlFor="email" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">
            Your email
          </label>
          <input
            type="email"
            id="email"
            className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
            placeholder="name@flowbite.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div className="mb-5">
          <label htmlFor="password" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">
            Your password
          </label>
          <input
            type="password"
            id="password"
            className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        {error && <div className="text-red-500 mb-3">{error}</div>}
        <button
          type="submit"
          className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
        >
          Submit
        </button>
      </form>
    </div>
  );
}

export default Login;
