import useAuth from '@/auth/store'
import { Spinner } from '@/components/ui/spinner';
import { refreshToken } from '@/services/AuthService';
import { useEffect, useState } from 'react'
import toast from 'react-hot-toast';
import { useNavigate } from 'react-router';

function OAuthSuccess() {

    const [isRefreshing, setIsRefreshing] = useState<boolean>(false);
    const changeLocalLoginData = useAuth(state => state.changeLocalLoginData);
    const navigate = useNavigate();
    useEffect(() => {

        async function getAccessToken() {

            if (!isRefreshing) {
                // refresh token ki api call krni hai
                setIsRefreshing(true);

                try {
                    const resLoginData = await refreshToken();
                    // login:
                    changeLocalLoginData(
                        resLoginData.accessToken,
                        resLoginData.user,
                        true);

                    toast.success("Login successfull !!")
                    navigate("/dashboard");
                } catch (error) {
                    toast.error("Error in login !!")
                    console.dir(error);
                } finally {
                    setIsRefreshing(false);
                }


            }
        }
        getAccessToken();
    }, [])


    return (
        <div className="p-10 flex flex-col gap-3 justify-center items-center">
            <Spinner />
            <h1 className="text-2xl font-semibold">Please wait....</h1>
        </div>
    )
}

export default OAuthSuccess