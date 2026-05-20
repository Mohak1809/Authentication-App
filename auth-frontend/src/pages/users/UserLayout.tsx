import useAuth from '@/auth/store'
import { Navigate, Outlet } from 'react-router'

function UserLayout() {

  const checkLogIn = useAuth((state) => state.checkLogin);

  if(checkLogIn())
  return (
    <div>
      <Outlet />
    </div>
  );
  else return <Navigate to={"/login"}/>
}

export default UserLayout