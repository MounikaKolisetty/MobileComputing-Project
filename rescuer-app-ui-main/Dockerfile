FROM node:latest AS build

#Env for backend apis
ENV REACT_APP_CREATE_TICKET=http://localhost:8080/ticket/create-ticket
ENV REACT_APP_LOGIN_URL=http://localhost:8080/login
ENV REACT_APP_GET_ALL_TICKETS=http://localhost:8080/ticket/all-tickets
ENV REACT_APP_GET_OPEN_TICKETS=http://localhost:8080/ticket/open-tickets
ENV REACT_APP_TICKET_DETAILS=http://localhost:8080/ticket/
ENV REACT_APP_GET_CLOSE_TICKETS=http://localhost:8080/ticket/closed-tickets
ENV REACT_APP_GET_IN_PROGRESS_TICKETS=http://localhost:8080/ticket/inProgress-tickets
ENV REACT_APP_ADD_RESCUER=http://localhost:8080/admin/add-rescuer
ENV REACT_APP_ADD_ADMIN=http://localhost:8080/admin/add-admin
ENV REACT_APP_ASSIGN_TICKET=http://localhost:8080/admin/assign-ticket/
ENV REACT_APP_RESOLVE_TICKET=http://localhost:8080/ticket/resolve/
ENV REACT_APP_REASSIGN_TICKET=http://localhost:8080/ticket/reopen/
ENV REACT_APP_DOWNLOAD_FILES=http://localhost:8080/file/download/

WORKDIR /app
COPY . .
RUN npm i && npm run build

FROM nginx:1.16.0-alpine
COPY --from=build /app/build /usr/share/nginx/html
RUN rm /etc/nginx/conf.d/default.conf
COPY nginx/nginx.conf /etc/nginx/conf.d
CMD ["nginx", "-g", "daemon off;"]