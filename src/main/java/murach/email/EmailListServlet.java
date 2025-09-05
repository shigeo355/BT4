package murach.email;

import java.io.IOException;
import java.time.Year;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import murach.business.User;
import murach.data.UserDB;

@WebServlet("/emailList")   // mapping servlet
public class EmailListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String url = "/index.jsp";

        // thêm năm hiện tại (ví dụ footer)
        int currentYear = Year.now().getValue();
        request.setAttribute("currentYear", currentYear);

        // lấy action
        String action = request.getParameter("action");
        if (action == null) {
            action = "join";  // mặc định
        }

        // xử lý action
        if (action.equals("join")) {
            url = "/index.jsp";
        } else if (action.equals("add")) {
            // lấy tham số từ form
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");

            // tạo đối tượng User
            User user = new User(firstName, lastName, email);

            // validate dữ liệu
            String message;
            if (firstName == null || lastName == null || email == null ||
                firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
                message = "Please fill out all three text boxes.";
                url = "/index.jsp";
            } else {
                message = null;
                url = "/thanks.jsp";
                UserDB.insert(user);   // lưu vào DB
            }

            request.setAttribute("user", user);
            request.setAttribute("message", message);
        }

        // forward sang JSP
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
