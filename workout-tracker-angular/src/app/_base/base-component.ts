import { Observable, BehaviorSubject } from 'rxjs';
import { AuthService } from '../_services/auth.service';

export class ComponentBase {

    private loggedIn: Boolean = false;
    private unauthorized$ = new BehaviorSubject<boolean>(true);

    constructor(private authservice: AuthService) {
        this.authservice.isAuthenticated().subscribe(
            (authenticated: boolean) => {
                this.loggedIn = Boolean(authenticated);
                this.unauthorized$.next(!authenticated);
            }
        );
    }

    isLoggedIn(): boolean {
        return this.loggedIn.valueOf();
    }

    protected isUnauthorized$(): Observable<boolean> {
        return this.unauthorized$.asObservable();
    }

    signout() {
        this.authservice.signout();
    }
}
