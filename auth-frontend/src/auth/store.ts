import type LoginData from "@/models/LoginData";
import type LoginResponseData from "@/models/LoginResponseData";
import type User from "@/models/User";
import { loginUser, logoutUser } from "@/services/AuthService";
import { create } from "zustand";
import { persist } from 'zustand/middleware'


const TOKEN_KEY = "app_state";

// type AuthStatus = "idle" | "authenticating" | "authenticated" | "anonymous";




type AuthState = {
  accessToken: string | null;
  user: User | null;
  authStatus: boolean;
  authLoading: boolean;
  login: (loginData: LoginData) => Promise<LoginResponseData>;
  logout: (silent?: boolean) => void;
  checkLogin: () => boolean | undefined;

  changeLocalLoginData: (access: string, user: User, authStatus: boolean) => void;
};


// main logic for global state
const useAuth = create<AuthState>()(
  persist(
    (set, get) => ({
      accessToken: null,
      user: null,
      authStatus: false,
      authLoading: false,

      changeLocalLoginData: (accessToken, user, authStatus) => {
        set({
          accessToken,
          user,
          authStatus
        });
      },
      login: async (loginData) => {
        console.log("started login...")
        set({ authLoading: true })
        try {
          const response = await loginUser(loginData);
          console.log(response)

          set({
            accessToken: response.accessToken,
            user: response.user,
            authStatus: true
          });
          return response;
        } catch (error) {
          console.log(error)
          throw error;
        } finally {
          set({ authLoading: false })
        }
      },

      logout: async (silent = false) => {
        try {
          // if(!silent) {
          //   await logoutUser();
          // }
          set({ authLoading: true });
          await logoutUser();

        } catch (error) {
        } finally {
          set({ authLoading: false });
        }
        set({
          accessToken: null,
          user: null,
          authStatus: false,
          authLoading: false,
        });


      },
      checkLogin: () => {
        if (get().accessToken && get().authStatus)
          return true;
        return false;
      },


    }),
    {
      name: TOKEN_KEY,
    }
  )
);

export default useAuth;