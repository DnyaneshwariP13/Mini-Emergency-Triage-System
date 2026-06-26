import { Routes } from '@angular/router';
import { Register } from './register/register';
import { Login } from './login/login';
import { Patient } from './patient/patient';
import { Patientform } from './patientform/patientform';
import { Triageform } from './triageform/triageform';
import { Viewpatient } from './viewpatient/viewpatient';
import { Dashboard } from './dashboard/dashboard';

export const routes: Routes = [
    
        {path: 'register', component: Register},
        {path: 'login', component: Login},
        {path: 'patients', component: Patient},
        {path: 'patients/add', component: Patientform},
        {path: 'patients/:id/triage', component: Triageform},
        {path: 'patients/update/:id', component: Patientform},
        {path: 'patients/:id', component: Viewpatient},
        { path: 'dashboard', component: Dashboard },
];

