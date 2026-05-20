import React from "react";
import { Button } from "./ui/button";
// import { Chrome, Github } from "lucide-react";
import { FaGithub } from "react-icons/fa";
import { FaGoogle } from "react-icons/fa";

import { NavLink } from "react-router";

function OAuth2Buttons() {
  return (
    <div className="space-y-3">
      <NavLink
        to={`${
          import.meta.env.VITE_BASE_URL || "http://localhost:8083"
        }/oauth2/authorization/google`}
        className={"block"}
      >
        <Button
          type="button"
          variant="outline"
          className="w-full cursor-pointer flex items-center gap-3 rounded-2xl"
        >
          <FaGoogle /> Continue with Google
        </Button>
      </NavLink>

      <NavLink
        to={`${
          import.meta.env.VITE_BASE_URL || "http://localhost:8083"
        }/oauth2/authorization/github`}
        className={"block"}
      >
        <Button
          type="button"
          variant="outline"
          className="w-full flex cursor-pointer items-center gap-3 rounded-2xl"
        >
          <FaGithub /> Continue with GitHub
        </Button>
      </NavLink>
    </div>
  );
}

export default OAuth2Buttons;