import Link from '@material-ui/core/Link';
import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Title from './Title';
import React, { useEffect, useState } from "react";
import instance from '../Axios';
import { Button } from '@material-ui/core';

function preventDefault(event) {
  event.preventDefault();
}

const useStyles = makeStyles((theme) => ({
  seeMore: {
    marginTop: theme.spacing(3),
  },
}));

export default function ClosedTickets() {
  const [error, setError] = useState(null);
  const [isLoaded, setIsLoaded] = useState(false);
  const [tickets, setTickets] = useState([[]]);
  const [isTicketAssigned, setTicketAssignStatus] = useState(false);
  useEffect(() => {
    getClosedTickets();
  }, [])

  function getClosedTickets(){
    instance.get(process.env.REACT_APP_GET_CLOSE_TICKETS)
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
  }
  function reAssignTicket(ticketId, userName){
    setTicketAssignStatus(true);
    instance.post(process.env.REACT_APP_REASSIGN_TICKET+ticketId,null, { params: {
      userName
    }})
      .then(
        (result) => {
          setTicketAssignStatus(result.data);
          getClosedTickets();
        },
        (error) => {
          setTicketAssignStatus(false);
          setError(error);
        }
      )
  }
  
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
              <TableCell align="right">Action</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {tickets.map((ticket) => (
              <TableRow key={ticket.ticketId}>
                <TableCell>{ticket.ticketId}</TableCell>
                <TableCell>{ticket.ticketStatus}</TableCell>
                <TableCell>{ticket.allocatedTo}</TableCell>
                <TableCell>{ticket.createdAt}</TableCell>
                <TableCell>
                    <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            color="primary"
                            onClick={()=>reAssignTicket(ticket.ticketId,ticket.allocatedTo)}
                        >
                        Re-Open
                    </Button>
                </TableCell>
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