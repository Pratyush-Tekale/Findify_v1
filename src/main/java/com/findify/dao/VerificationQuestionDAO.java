package com.findify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.findify.model.VerificationQuestion;
import com.findify.util.DBConnection;

public class VerificationQuestionDAO {

    // Save one question+answer pair against a found item.
    public boolean addQuestion(int foundId, String questionText, String correctAnswer) {

        try (Connection con = DBConnection.getConnection()) {

            String sql =
                "INSERT INTO verification_questions(found_id, question_text, correct_answer) VALUES(?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, foundId);
            ps.setString(2, questionText);
            ps.setString(3, correctAnswer);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Full rows, INCLUDING correct_answer. Server-side use only (scoring a
     * submitted claim, or the admin review screen) — never send this
     * straight to a public-facing page.
     */
    public List<VerificationQuestion> getQuestionsByFoundId(int foundId) {

        List<VerificationQuestion> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection()) {

            String sql =
                "SELECT * FROM verification_questions WHERE found_id = ? ORDER BY question_id";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, foundId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                VerificationQuestion q = new VerificationQuestion();
                q.setQuestionId(rs.getInt("question_id"));
                q.setFoundId(rs.getInt("found_id"));
                q.setQuestionText(rs.getString("question_text"));
                q.setCorrectAnswer(rs.getString("correct_answer"));
                list.add(q);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
