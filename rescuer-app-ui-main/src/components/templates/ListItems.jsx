import React from 'react';
import {NavLink} from 'react-router-dom';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import DashboardIcon from '@material-ui/icons/Dashboard';
import ConfirmationNumberIcon from '@material-ui/icons/ConfirmationNumber';
import LayersIcon from '@material-ui/icons/Layers';
import AssignmentIcon from '@material-ui/icons/Assignment';
import AppStorage from '../../service/appStorage';

import SupervisorAccountIcon from '@material-ui/icons/SupervisorAccount';
import PersonAddIcon from '@material-ui/icons/PersonAdd';
import paths from '../../routes/paths';

export const MainListItems = () => {
  const userType = AppStorage.getItem('user_type');
  const isAdmin = userType && userType === 'ADMIN';
  const isRescuer = userType && userType === 'RESCUER';
  if(isAdmin || isRescuer) {
    return (
      <div>
      <ListItem button>
        <ListItemIcon>
          <DashboardIcon />
        </ListItemIcon>
        <NavLink
          to="/dashboard"
        >
          <ListItemText primary="Dashboard" />
        </NavLink>
      </ListItem>
      <ListItem button hide={isRescuer}>
        <ListItemIcon>
          <ConfirmationNumberIcon />
        </ListItemIcon>
        <NavLink
          to="/tickets/open-tickets"
        >
          <ListItemText primary="Open Tickets" />
        </NavLink>
      </ListItem>
      <ListItem button>
        <ListItemIcon>
          <AssignmentIcon />
        </ListItemIcon>
        <NavLink
          to="/tickets/closed-tickets"
        >
          <ListItemText primary="Closed Tickets" />
        </NavLink>
      </ListItem>
      <ListItem button>
        <ListItemIcon>
          <ConfirmationNumberIcon />
        </ListItemIcon>
        <NavLink
          to="/tickets/in-progress-tickets"
        >
          <ListItemText primary="In-Progress Tickets" />
        </NavLink>
      </ListItem>
      {isAdmin && <ListItem button>
        <ListItemIcon>
          <SupervisorAccountIcon />
        </ListItemIcon>
        <NavLink
          to={paths.addAdmin.path}
        >
          <ListItemText primary="Add Admin" />
        </NavLink>
      </ListItem>}
      {isAdmin && <ListItem button>
        <ListItemIcon>
          <PersonAddIcon />
        </ListItemIcon>
        <NavLink
          to={paths.addRescuer.path}
        >
          <ListItemText primary="Add Rescuer" />
        </NavLink>
      </ListItem>}
    </div>
    )
  }
  return <></>
}

