import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
        urlPatterns = { "*.do" }
)
public class TodoMVCServlet extends HttpServlet {
    private final Repository repository;

    public TodoMVCServlet(Repository repo) {
        this.repository = repo;
    }

    public TodoMVCServlet() {
        this.repository = new InMemoryRepository();
        System.out.println("Created empty InMemoryRepository repository");

//        insertDummyTodos();
    }

    private void insertDummyTodos() {
        /**
         * Done just to have a different times of Todo insertion
         */
        try {
            this.repository.save(new Todo("Learn Servlets"));
            Thread.sleep(1000);
            this.repository.save(new Todo(new Todo("Completed"), Todo.Status.COMPLETED));
        } catch (InterruptedException e) {
        } finally {
            System.out.println("Inserted " + this.repository.count() + " dummy todos");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);
    }

    private void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log("Handling request");
        String command = parseCommand(req);
        String view = dispatchControl(command, req, resp);
        forwardToView(view, req, resp);
    }

    String parseCommand(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        int lastSlashIdx = requestURI.lastIndexOf("/");
        int lastDotIdx = requestURI.lastIndexOf(".");
        return requestURI.substring(lastSlashIdx + 1, lastDotIdx);
    }

    String dispatchControl(String command, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //TODO: Replace with switch
        if (command.equals("index")) {
            Collection<Todo> all = this.repository.findAll();

            req.setAttribute("todos", all);

            List<Todo> completed = all.stream().filter(p -> p.getStatus() == Todo.Status.COMPLETED)
                    .collect(Collectors.toList());
            req.setAttribute("completed", completed);

            return "index";
        }
        else if (command.equals("new")) {
            String newTodoTitle = req.getParameter("new-todo");
            Todo newTodo = new Todo(newTodoTitle);

            this.repository.save(newTodo);

            resp.sendRedirect("index.do");
            return "index";
        }
        else if (command.equals("toggle")) {
            Long todoId = Long.parseLong(req.getParameter("todo-id"));
            Todo todo = this.repository.findOne(todoId);
            Todo.Status toggledStatus = todo.getStatus() == Todo.Status.ACTIVE ? Todo.Status.COMPLETED : Todo.Status.ACTIVE;

            Todo changedTodo = new Todo(todo, toggledStatus);
            this.repository.save(changedTodo);

            resp.sendRedirect("index.do");
            return "";
        }
        else if (command.equals("delete")) {
            Long todoId = Long.parseLong(req.getParameter("todo-id"));

            this.repository.delete(todoId);

            resp.sendRedirect("index.do");
            return "";
        }
        else if (command.equals("clear")) {
            // TODO: This is buggy
            Collection<Todo> all = this.repository.findAll();

            for (Todo todo : all) {
                if (todo.getStatus() == Todo.Status.COMPLETED) {
                    this.repository.delete(todo);
                }
            }

            resp.sendRedirect("index.do");
            return "";
        }
        else {
            return "index";
        }
    }

    void forwardToView(String view, HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        if (!resp.isCommitted()) {
            String path = "/" + view + ".jsp";
            RequestDispatcher dispatcher = req.getRequestDispatcher(path);
            dispatcher.forward(req, resp);
        }
    }
}