package com.findify.servlet;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.findify.dao.ClaimDAO;
import com.findify.dao.VerificationQuestionDAO;
import com.findify.model.Claim;
import com.findify.model.ClaimAnswer;
import com.findify.model.User;
import com.findify.model.VerificationQuestion;
import com.findify.util.AnswerMatcher;

import jakarta.servlet.http.HttpSession;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ClaimServlet")
public class ClaimServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public ClaimServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int foundId = Integer.parseInt(request.getParameter("foundId"));

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Load the private questions (with their correct answers) for this
        // found item — never sent back to the browser, used only here to
        // score what the claimant just submitted.
        VerificationQuestionDAO qDao = new VerificationQuestionDAO();
        List<VerificationQuestion> questions = qDao.getQuestionsByFoundId(foundId);

        if (questions.isEmpty()) {
            response.sendRedirect("verify.jsp?foundId=" + foundId + "&error=noquestions");
            return;
        }

        int matched = 0;
        List<ClaimAnswer> submittedAnswers = new ArrayList<>();

        for (VerificationQuestion q : questions) {

            String submitted = request.getParameter("answer_" + q.getQuestionId());

            boolean isCorrect = AnswerMatcher.isMatch(q.getCorrectAnswer(), submitted);

            if (isCorrect) {
                matched++;
            }

            ClaimAnswer answer = new ClaimAnswer();
            answer.setQuestionId(q.getQuestionId());
            answer.setSubmittedAnswer(submitted);
            answer.setCorrect(isCorrect);
            submittedAnswers.add(answer);
        }

        Claim claim = new Claim();

        claim.setFoundId(foundId);
        claim.setClaimantId(loggedInUser.getUserId());
        claim.setStatus("PENDING");
        claim.setMatchedAnswers(matched);
        claim.setTotalQuestions(questions.size());

        ClaimDAO dao = new ClaimDAO();

        boolean success = dao.addClaim(claim, submittedAnswers);

        if (success) {
            response.sendRedirect("claim.html");
        } else {
            response.sendRedirect("verify.jsp?foundId=" + foundId);
        }
    }
}
