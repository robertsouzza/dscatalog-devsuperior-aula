import { hasAnyRoles } from "../auth";
import * as TokenModule from '../token';

describe('hasAnyRoles tests', () => {

    test('should return true when empty list', () => {
        const result = hasAnyRoles([]);
        expect(result).toEqual(true);
    });

    test('should return true when user has given role', () => { // ou seja a minha função hasAnyRoles tem quetornar verdadeiro quando o usuário possuir o role que for passado de argumento 
        
        jest.spyOn(TokenModule,'getTokenData').mockReturnValue({
            exp: 0,
            user_name: 'roberto',
            authorities: ['ROLE_ADMIN','ROLE_OPERATOR']
        })

        const result = hasAnyRoles(['ROLE_ADMIN']);              
        expect(result).toEqual(true);
    });
    
    test('should return false when user  does not have given role', () => { // sado de argumento 
        
        jest.spyOn(TokenModule,'getTokenData').mockReturnValue({
            exp: 0,
            user_name: 'roberto',
            authorities: ['ROLE_OPERATOR']
        })

        const result = hasAnyRoles(['ROLE_ADMIN']);              
        expect(result).toEqual(false);
    });

});

