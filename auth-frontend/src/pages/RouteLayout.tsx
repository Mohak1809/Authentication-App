import React from 'react'
import { Outlet } from 'react-router'
import Navbar from '../components/Navbar'
import { Toaster } from 'react-hot-toast'


function RouteLayout() {
  return (
    <div>
      <Toaster />
        <Navbar />
        <Outlet />
    </div>
  )
}

export default RouteLayout

// Nested routes