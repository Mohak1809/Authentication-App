import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Route, Routes } from "react-router";
import './index.css'
import App from './App.tsx'
import Login from './pages/Login.tsx';
import SignUp from './pages/SignUp.tsx';
import Services from './pages/Services.tsx';
import About from './pages/About.tsx';
import RouteLayout from './pages/RouteLayout.tsx';
import UserLayout from './pages/users/UserLayout.tsx';
import Userhome from './pages/users/Userhome.tsx';
import Userprofile from './pages/users/Userprofile.tsx';
import OAuthSuccess from './pages/OAuthSuccess.tsx';
import OAuthFailure from './pages/OAuthFailure.tsx';

createRoot(document.getElementById('root')!).render(
  <BrowserRouter>
    <Routes>
      <Route path='/' element={<RouteLayout />}>
        <Route index element={<App />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/services" element={<Services />} />
        <Route path="/about" element={<About />} />
        <Route path="/dashboard" element={<UserLayout />} >
          <Route index element={<Userhome />} />
          <Route path="profile" element={<Userprofile />} />
          {/* protect all routes.... */}
        </Route>
        <Route path='auth/success' element={<OAuthSuccess />}/>
        <Route path='auth/failure' element={<OAuthFailure />}/>
      </Route>
    </Routes>
  </BrowserRouter>,
)
