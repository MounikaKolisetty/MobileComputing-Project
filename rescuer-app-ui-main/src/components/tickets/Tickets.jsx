import Link from '@material-ui/core/Link';
import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Title from './Title';
import { useHistory } from 'react-router-dom';
import React, { useEffect, useState } from "react";
import instance from '../Axios';

function preventDefault(event) {
  event.preventDefault();
}

const useStyles = makeStyles((theme) => ({
  seeMore: {
    marginTop: theme.spacing(3),
  },
}));

export default function Tickets() {
  const [error, setError] = useState(null);
  const [isLoaded, setIsLoaded] = useState(false);
  const [tickets, setTickets] = useState([[]]);
  const history = useHistory();
  useEffect(() => {
    instance.get(process.env.REACT_APP_GET_ALL_TICKETS)
      .then(
        (result) => {
          setIsLoaded(true);
          setTickets(result.data);
        },
        (error) => {
          setIsLoaded(true);
          setError(error);
        }
      )
  }, []);

  const redirectToTicketDetailsPage = (ticket) => {
    history.push('tickets/ticket-details/'+ticket);
  };
  
  const classes = useStyles();
  if(isLoaded){
    console.log(tickets);
    return (
      <React.Fragment>
        <Title>Tickets</Title>
        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell>Ticket Id</TableCell>
              <TableCell>Ticket Status</TableCell>
              <TableCell>Allocated to</TableCell>
              <TableCell>created At</TableCell>
              <TableCell align="right">Required Items</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {tickets.map((ticket) => (
              <TableRow key={ticket.ticketId} >
                <TableCell onClick={(e) => redirectToTicketDetailsPage(ticket.ticketId)}>{ticket.ticketId}</TableCell>
                <TableCell>{ticket.ticketStatus}</TableCell>
                <TableCell>{ticket.allocatedTo}</TableCell>
                <TableCell>{ticket.createdAt}</TableCell>
                <TableCell>{ticket.requiredItems}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </React.Fragment>
    );
  }else {
    return <div>Loading...</div>;
  }
  
}