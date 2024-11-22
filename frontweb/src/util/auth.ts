import { getTokenData } from './token';
import { Role } from 'types/role';

export const isAuthenticated = (): boolean => {
    const TokenData = getTokenData();
    return (TokenData && TokenData.exp * 1000 > Date.now()) ? true : false;
};

export const hasAnyRoles = (roles: Role[]): boolean => {

    if (roles.length === 0) {
        return true;
    }

    const tokenData = getTokenData();

    if (tokenData !== undefined) {
        for (var i = 0; i < roles.length; i++) {
            if (tokenData.authorities.includes(roles[i])) {
                return true;
            }
        }
    }

    return false;
}