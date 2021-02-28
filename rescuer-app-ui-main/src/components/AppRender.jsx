import React, {Suspense, lazy} from 'react';
import {
    BrowserRouter as Router,
    Switch,
    Route,
    Redirect
} from "react-router-dom";

import routePaths from '../routes/paths';
import SecureLayout from '../components/templates/SecureTemplate';
import OpenTicketsView from './home/OpenTicketsView';
import ClosedTicketsView from './home/ClosedTicketsView';
import InProgressTicketsView from './home/InProgressTicketsView';
import BroadcastListener from './BroadcastListener';
import AddRescuer from './user/AddRescuer';
import AddAdmin from './user/AddAdmin';
import TicketDetails from './tickets/TicketDetails';

const DashboardPage = lazy(() => import('../components/home/'));
const LoginPage = lazy(() => import('../components/login'));

function ComponentLoader() {
    return <h2>Loading</h2>
}

function PageRenderer() {
    return(
        <Router>
            <BroadcastListener />
            <Suspense fallback={<ComponentLoader />}>
            <Switch>
                <Redirect exact={true} from="/" to={routePaths.dashboard.path} />
                <Route
                    path={routePaths.dashboard.path}
                    render={() => (
                        <SecureLayout pageName={"Dashboard"}>
                            <DashboardPage />
                        </SecureLayout>
                    )}
                />
                <Route
                    path={routePaths.ticketDetails.path}
                    render={() => (
                        <SecureLayout pageName={"Ticket Details"}>
                            <TicketDetails />
                        </SecureLayout>
                    )}
                />
                <Route
                    path={routePaths.openTickets.path}
                    render={() => (
                        <SecureLayout pageName={"Open Tickets"}>
                            <OpenTicketsView />
                        </SecureLayout>
                    )}
                />
                <Route
                    path={routePaths.closedTickets.path}
                    render={() => (
                        <SecureLayout pageName={"Closed Tickets"}>
                            <ClosedTicketsView />
                        </SecureLayout>
                    )}
                />
                <Route
                    path={routePaths.inProgressTickets.path}
                    render={() => (
                        <SecureLayout pageName={"In Progress Tickets"}>
                            <InProgressTicketsView />
                        </SecureLayout>
                    )}
                />
                <Route
                    path={routePaths.login.path}
                    render={() => (
                        <LoginPage />
                    )}
                />
                <Route
                    path={routePaths.addAdmin.path}
                    render={() => (
                        <SecureLayout pageName={"Add Admin"}>
                            <AddAdmin />
                        </SecureLayout>
                    )}
                />
                <Route
                    path={routePaths.addRescuer.path}
                    render={() => (
                        <SecureLayout pageName={"Add Rescuer"}>
                            <AddRescuer />
                        </SecureLayout>
                    )}
                />
            </Switch>
            </Suspense>
            
        </Router>
    );
}

  export default PageRenderer;