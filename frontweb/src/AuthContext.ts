import { createContext } from "react";
import { TokenData } from "util/token";

export type AuthContextData = {
    authenticated: boolean;
    tokenData?: TokenData; //opcional preencher o token
};

export type AuthcontextType = {
    authContextData: AuthContextData,
    setAuthContextData: (authContextData: AuthContextData) => void;
};

export const AuthContext = createContext<AuthcontextType>({

    authContextData: {
        authenticated: false
    },
    setAuthContextData: () => null
});