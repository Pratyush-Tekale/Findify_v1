package com.findify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.findify.model.ClaimAnswer;
import com.findify.util.DBConnection;

public class ClaimAnswerDAO {

    public boolean addAnswer(Connection con, int claimId, int questionId,
            String submittedAnswer, boolean isCorrect) throws SQLException {

        String sql =
            "INSERT INTO claim_answers(claim_id, question_id, submitted_answer, is_correct) VALUES(?,?,?,?)";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, claimId);
        ps.setInt(2, questionId);
        ps.setString(3, submittedAnswer);
        ps.setBoolean(4, isCorrect);

        return ps.executeUpdate() > 0;
    }

    // Per-question breakdown (question text, correct answer, submitted
    // answer, match) for the admin claim-detail view.
    public List<ClaimAnswer> getAnswersByClaimId(int claimId) {

        List<ClaimAnswer> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection()) {

            String sql =
                "SELECT ca.*, vq.question_text, vq.correct_answer " +
                "FROM claim_answers ca " +
                "JOIN verification_questions vq ON ca.question_id = vq.question_id " +
                "WHERE ca.claim_id = ? " +
                "ORDER BY ca.answer_id";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, claimId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ClaimAnswer a = new ClaimAnswer();
                a.setAnswerId(rs.getInt("answer_id"));
                a.setClaimId(rs.getInt("claim_id"));
                a.setQuestionId(rs.getInt("question_id"));
                a.setSubmittedAnswer(rs.getString("submitted_answer"));
                a.setCorrect(rs.getBoolean("is_correct"));
                a.setQuestionText(rs.getString("question_text"));
                a.setCorrectAnswer(rs.getString("correct_answer"));
                list.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
