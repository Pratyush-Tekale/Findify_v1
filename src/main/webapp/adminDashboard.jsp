<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.findify.model.User" %>

<%
    User loggedInUser =
        (User) session.getAttribute("loggedInUser");

    if (loggedInUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    if (!"ADMIN".equals(loggedInUser.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
    SimpleDateFormat foundDateFormat = new SimpleDateFormat("dd MMM yyyy");
    SimpleDateFormat activityDateFormat = new SimpleDateFormat("dd MMM, HH:mm");

    String searchValue = (String) request.getAttribute("searchValue");
    String statusValue = (String) request.getAttribute("statusValue");
    String viewLabel = (String) request.getAttribute("viewLabel");

    List<?> pendingClaims =
        (List<?>) request.getAttribute("pendingClaims");

    List<?> recentActivity =
        (List<?>) request.getAttribute("recentActivity");
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Findify | Admin Dashboard</title>

<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/admiin.css">
</head>

<body>

<header class="site-header">
  <div class="wrap">
    <nav>

      <a href="AdminDashboardServlet" class="logo">
        FINDIFY <span class="admin-badge">Admin</span>
      </a>

      <div class="links">
        <a href="index.jsp">Home</a>
        <a href="#pendingClaims">Manage Claims</a>
        <a href="ViewFoundServlet">Found Items</a>
        <a href="BrowseLostItemsServlet">Lost Items</a>
      </div>

      <a href="LogoutServlet" class="nav-cta ghost">Logout</a>

    </nav>
  </div>
</header>


<section class="section">
<div class="wrap">

  <!-- Welcome -->
  <div class="admin-welcome">

    <div class="welcome-chip">

      <div class="av">
        <%= loggedInUser.getFullName() != null &&
            !loggedInUser.getFullName().isEmpty()
            ? loggedInUser.getFullName().substring(0, 1)
            : "A" %>
      </div>

      <span>
        Welcome, <%= loggedInUser.getFullName() %>
      </span>

    </div>

    <p class="section-sub" style="margin:0;">
      Manage campus claims and monitor all lost &amp; found activity.
    </p>

  </div>


  <!-- Statistics -->
  <div class="admin-stat-grid six">

    <div class="admin-stat-card">
      <div class="a-num">${totalUsers}</div>
      <div class="a-label">Total Users</div>
    </div>

    <div class="admin-stat-card">
      <div class="a-num">${totalLostItems}</div>
      <div class="a-label">Lost Items</div>
    </div>

    <div class="admin-stat-card">
      <div class="a-num">${totalFoundItems}</div>
      <div class="a-label">Found Items</div>
    </div>

    <div class="admin-stat-card pending">
      <div class="a-num">${pendingClaimsCount}</div>
      <div class="a-label">Pending Claims</div>
    </div>

    <div class="admin-stat-card approved">
      <div class="a-num">${approvedClaimsCount}</div>
      <div class="a-label">Approved Claims</div>
    </div>

    <div class="admin-stat-card rejected">
      <div class="a-num">${rejectedClaimsCount}</div>
      <div class="a-label">Rejected Claims</div>
    </div>

  </div>


  <!-- Search -->
  <form action="AdminDashboardServlet" method="get" class="filter-bar">

    <input
      type="text"
      name="search"
      placeholder="Search Item"
      value="<%= searchValue != null ? searchValue : "" %>">

    <select name="status">

      <option value=""
        <%= "".equals(statusValue) || statusValue == null
            ? "selected" : "" %>>
        All Status
      </option>

      <option value="PENDING"
        <%= "PENDING".equals(statusValue)
            ? "selected" : "" %>>
        Pending
      </option>

      <option value="APPROVED"
        <%= "APPROVED".equals(statusValue)
            ? "selected" : "" %>>
        Approved
      </option>

      <option value="REJECTED"
        <%= "REJECTED".equals(statusValue)
            ? "selected" : "" %>>
        Rejected
      </option>

    </select>

    <button type="submit">Search</button>

    <%
        if ((searchValue != null && !searchValue.trim().isEmpty()) ||
            (statusValue != null && !statusValue.trim().isEmpty())) {
    %>

      <a href="AdminDashboardServlet"
         class="btn btn-reject"
         style="text-decoration:none;">
        Clear
      </a>

    <%
        }
    %>

  </form>


  <!-- Dashboard columns -->
  <div class="dash-columns">

    <div>

      <div class="section-head" id="pendingClaims">

        <h2 class="section-title">
          <%= viewLabel != null ? viewLabel : "Claims" %>
        </h2>

        <p class="section-sub">
          Review and verify all submitted claim requests.
        </p>

      </div>


      <div class="table-wrap">

        <table class="table-ticket">

          <thead>
            <tr>
              <th>Claim ID</th>
              <th>Item</th>
              <th>Claimed By</th>
              <th>Phone</th>
              <th>Verification</th>
              <th>Claim Date</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>


          <tbody>

          <%
              if (pendingClaims == null || pendingClaims.isEmpty()) {
          %>

            <tr>
              <td colspan="8" style="text-align:center;">
                No Claims Found
              </td>
            </tr>

          <%
              } else {

                  for (Object obj : pendingClaims) {

                      com.findify.model.Claim claim =
                          (com.findify.model.Claim) obj;

                      int confidence = claim.getAiConfidence();

                      String confidenceClass;

                      if (confidence >= 75) {
                          confidenceClass = "high";
                      } else if (confidence >= 40) {
                          confidenceClass = "mid";
                      } else {
                          confidenceClass = "low";
                      }
          %>


            <tr>

              <!-- Claim ID -->
              <td>
                <span class="id-chip">
                  #<%= claim.getClaimId() %>
                </span>
              </td>


              <!-- Item -->
              <td>
                <%= claim.getItemName() != null
                    ? claim.getItemName()
                    : "" %>
              </td>


              <!-- Claimant -->
              <td>
                <%= claim.getClaimantName() != null
                    ? claim.getClaimantName()
                    : "" %>
              </td>


              <!-- Phone -->
              <td>
                <%= claim.getClaimantPhone() != null
                    ? claim.getClaimantPhone()
                    : "" %>
              </td>


              <!-- AI Verification -->
              <td>

                <div class="trust-bar">

                  <div class="track">

                    <div
                      class="fill <%= confidenceClass %>"
                      style="width:<%= confidence %>%;">
                    </div>

                  </div>

                  <span>
                    <%= confidence %>% –
                    <%= claim.isAiMatch()
                        ? "AI: Match"
                        : "AI: No match" %>
                  </span>

                </div>

              </td>


              <!-- Claim Date -->
              <td>
                <%= claim.getClaimDate() != null
                    ? dateFormat.format(claim.getClaimDate())
                    : "" %>
              </td>


              <!-- Status -->
              <td>

                <%
                    if ("PENDING".equals(claim.getStatus())) {
                %>

                  <span class="badge badge-amber">
                    Pending
                  </span>

                <%
                    } else if ("APPROVED".equals(claim.getStatus())) {
                %>

                  <span class="badge badge-green">
                    Approved
                  </span>

                <%
                    } else {
                %>

                  <span class="badge badge-red">
                    Rejected
                  </span>

                <%
                    }
                %>

              </td>


              <!-- Actions -->
              <td>

                <button
                  type="button"
                  class="btn btn-view"

                  data-id="<%= claim.getClaimId() %>"

                  data-item="<%= claim.getItemName() != null
                      ? claim.getItemName().replace("\"", "&quot;")
                      : "" %>"

                  data-found-id="<%= claim.getFoundId() %>"

                  data-desc="<%= claim.getItemDescription() != null
                      ? claim.getItemDescription().replace("\"", "&quot;")
                      : "" %>"

                  data-location="<%= claim.getLocationFound() != null
                      ? claim.getLocationFound().replace("\"", "&quot;")
                      : "" %>"

                  data-date-found="<%= claim.getDateFound() != null
                      ? foundDateFormat.format(claim.getDateFound())
                      : "" %>"

                  data-image="<%= claim.getItemImage() != null
                      ? claim.getItemImage().replace("\"", "&quot;")
                      : "" %>"

                  data-image-base="<%= request.getContextPath() %>/uploads/"

                  data-claimant="<%= claim.getClaimantName() != null
                      ? claim.getClaimantName().replace("\"", "&quot;")
                      : "" %>"

                  data-claimant-id="<%= claim.getClaimantId() %>"

                  data-phone="<%= claim.getClaimantPhone() != null
                      ? claim.getClaimantPhone()
                      : "" %>"

                  data-ai-match="<%= claim.isAiMatch() %>"

                  data-ai-confidence="<%= claim.getAiConfidence() %>"

                  data-ai-reasoning="<%= claim.getAiReasoning() != null
                      ? claim.getAiReasoning().replace("\"", "&quot;")
                      : "" %>"

                  data-submitted-desc="<%= claim.getSubmittedDescription() != null
                      ? claim.getSubmittedDescription().replace("\"", "&quot;")
                      : "" %>"

                  data-status="<%= claim.getStatus() %>"

                  data-date="<%= claim.getClaimDate() != null
                      ? new SimpleDateFormat("dd MMM yyyy, HH:mm")
                          .format(claim.getClaimDate())
                      : "" %>"

                  onclick="openClaimModal(this.dataset)">

                  View

                </button>


                <%
                    if ("PENDING".equals(claim.getStatus())) {
                %>

                  <!-- Approve -->
                  <form
                    action="ManageClaimsServlet"
                    method="post"
                    style="display:inline;">

                    <input
                      type="hidden"
                      name="claimId"
                      value="<%= claim.getClaimId() %>">

                    <input
                      type="hidden"
                      name="action"
                      value="approve">

                    <button class="btn btn-approve">
                      Approve
                    </button>

                  </form>


                  <!-- Reject -->
                  <form
                    action="ManageClaimsServlet"
                    method="post"
                    style="display:inline;">

                    <input
                      type="hidden"
                      name="claimId"
                      value="<%= claim.getClaimId() %>">

                    <input
                      type="hidden"
                      name="action"
                      value="reject">

                    <button class="btn btn-reject">
                      Reject
                    </button>

                  </form>

                <%
                    } else {
                %>

                  <span class="completed">
                    Completed
                  </span>

                <%
                    }
                %>

              </td>

            </tr>


          <%
                  }
              }
          %>

          </tbody>

        </table>

      </div>

    </div>


    <!-- Recent Activity -->
    <div class="activity-panel">

      <h3>Recent Activity</h3>

      <ul class="activity-list">

      <%
          if (recentActivity == null || recentActivity.isEmpty()) {
      %>

        <li>Nothing yet.</li>

      <%
          } else {

              for (Object obj : recentActivity) {

                  com.findify.model.Claim activity =
                      (com.findify.model.Claim) obj;

                  String dotClass = "blue";

                  if ("APPROVED".equals(activity.getStatus())) {
                      dotClass = "green";
                  } else if ("REJECTED".equals(activity.getStatus())) {
                      dotClass = "red";
                  }
      %>

        <li>

          <span class="dot <%= dotClass %>"></span>

          <div>

            <div>

              <strong>
                <%= activity.getClaimantName() %>
              </strong>

              claimed

              <strong>
                <%= activity.getItemName() %>
              </strong>

              —

              <%
                  if ("APPROVED".equals(activity.getStatus())) {
              %>

                approved

              <%
                  } else if ("REJECTED".equals(activity.getStatus())) {
              %>

                rejected

              <%
                  } else {
              %>

                pending review

              <%
                  }
              %>

            </div>

            <span class="a-time">

              <%= activity.getClaimDate() != null
                  ? activityDateFormat.format(activity.getClaimDate())
                  : "" %>

            </span>

          </div>

        </li>

      <%
              }
          }
      %>

      </ul>

    </div>

  </div>

</div>
</section>


<!-- Footer -->
<footer class="site-footer">

  <div class="wrap">

    <div class="footer-bottom">

      <span>
        © 2026 FINDIFY
      </span>

      <span>
        Campus Lost &amp; Found Admin Panel
      </span>

    </div>

  </div>

</footer>


<!-- Claim Detail Modal -->
<div
  class="modal-overlay"
  id="claimModalOverlay"
  onclick="if(event.target===this) closeClaimModal()">

  <div class="modal-card">

    <div class="modal-head">

      <h3>
        Claim <span id="mClaimId"></span>
      </h3>

      <button
        type="button"
        class="modal-close"
        onclick="closeClaimModal()">

        &times;

      </button>

    </div>


    <div class="modal-body">

      <div class="modal-row">
        <span class="modal-label">Status</span>
        <span id="mStatus"></span>
      </div>

      <div class="modal-row">
        <span class="modal-label">AI Confidence</span>
        <span id="mTrust"></span>
      </div>

      <div
        id="mImageWrap"
        class="modal-row full"
        style="display:none;">

        <span class="modal-label">Item Photo</span>

        <img
          id="mImage"
          class="modal-image"
          alt="Item photo">

      </div>

      <div class="modal-row">
        <span class="modal-label">Item Claimed</span>
        <span id="mItem"></span>
      </div>

      <div class="modal-row full">

        <span class="modal-label">
          Item Description
        </span>

        <p id="mDesc" class="modal-proof"></p>

      </div>

      <div class="modal-row">
        <span class="modal-label">Found Location</span>
        <span id="mLocation"></span>
      </div>

      <div class="modal-row">
        <span class="modal-label">Date Found</span>
        <span id="mDateFound"></span>
      </div>

      <div class="modal-row">
        <span class="modal-label">Found Item Ref</span>
        <span id="mFoundId"></span>
      </div>

      <div class="modal-row">
        <span class="modal-label">Claimant</span>
        <span id="mClaimant"></span>
      </div>

      <div class="modal-row">
        <span class="modal-label">Claimant User Ref</span>
        <span id="mClaimantId"></span>
      </div>

      <div class="modal-row">
        <span class="modal-label">Phone</span>
        <span id="mPhone"></span>
      </div>

      <div class="modal-row">
        <span class="modal-label">Filed On</span>
        <span id="mDate"></span>
      </div>

      <div class="modal-row full">

        <span class="modal-label">
          AI Verification Details
        </span>

        <div
          id="mProof"
          class="modal-proof">
        </div>

      </div>

    </div>

  </div>

</div>


<script>

function openClaimModal(c) {

  document.getElementById('mClaimId').textContent =
    '#' + c.id;

  document.getElementById('mItem').textContent =
    c.item || '—';

  document.getElementById('mDesc').textContent =
    c.desc && c.desc !== 'null'
      ? c.desc
      : 'No description on file.';

  document.getElementById('mLocation').textContent =
    c.location && c.location !== 'null'
      ? c.location
      : '—';

  document.getElementById('mDateFound').textContent =
    c.dateFound || '—';

  document.getElementById('mFoundId').textContent =
    'found_id: ' + c.foundId;

  document.getElementById('mClaimant').textContent =
    c.claimant || '—';

  document.getElementById('mClaimantId').textContent =
    'user_id: ' + c.claimantId;

  document.getElementById('mPhone').textContent =
    c.phone || '—';

  document.getElementById('mDate').textContent =
    c.date || '—';


  /*
   * AI verification
   */
  document.getElementById('mProof').innerHTML =

    '<div class="ai-detail">' +
      '<strong>AI verdict:</strong> ' +
      (c.aiMatch === 'true'
        ? 'Match'
        : 'No match') +
      ' (' +
      (c.aiConfidence || 0) +
      '% confidence)' +
    '</div>' +

    '<div class="ai-detail">' +
      '<strong>AI reasoning:</strong>' +
      '<div>' +
      (c.aiReasoning || 'No reasoning available.') +
      '</div>' +
    '</div>' +

    '<div class="ai-detail">' +
      '<strong>Claimant description:</strong>' +
      '<div>' +
      (c.submittedDesc || 'No description provided.') +
      '</div>' +
    '</div>' +

    '<div class="ai-detail">' +
      '<strong>Finder private description:</strong>' +
      '<div>' +
      (c.desc || 'No description on file.') +
      '</div>' +
    '</div>';


  /*
   * Image
   */
  var imgWrap =
    document.getElementById('mImageWrap');

  var img =
    document.getElementById('mImage');

  if (
    c.image &&
    c.image !== 'null' &&
    c.image.trim() !== ''
  ) {

    img.src =
      c.imageBase +
      encodeURIComponent(c.image);

    imgWrap.style.display = 'flex';

  } else {

    imgWrap.style.display = 'none';

  }


  /*
   * Confidence
   */
  var confidence =
    Number(c.aiConfidence || 0);

  var confidenceClass;

  if (confidence >= 75) {
    confidenceClass = 'high';
  } else if (confidence >= 40) {
    confidenceClass = 'mid';
  } else {
    confidenceClass = 'low';
  }

  document.getElementById('mTrust').innerHTML =

    '<span class="trust-pill ' +
    confidenceClass +
    '">' +

    confidence +
    '% confidence (' +

    (c.aiMatch === 'true'
      ? 'AI Match'
      : 'AI No Match') +

    ')</span>';


  /*
   * Status
   */
  var statusHtml;

  if (c.status === 'PENDING') {

    statusHtml =
      '<span class="badge badge-amber">' +
      'Pending' +
      '</span>';

  } else if (c.status === 'APPROVED') {

    statusHtml =
      '<span class="badge badge-green">' +
      'Approved' +
      '</span>';

  } else {

    statusHtml =
      '<span class="badge badge-red">' +
      'Rejected' +
      '</span>';

  }

  document.getElementById('mStatus').innerHTML =
    statusHtml;


  document
    .getElementById('claimModalOverlay')
    .classList
    .add('open');
}


function closeClaimModal() {

  document
    .getElementById('claimModalOverlay')
    .classList
    .remove('open');

}


document.addEventListener(
  'keydown',
  function(e) {

    if (e.key === 'Escape') {
      closeClaimModal();
    }

  }
);

</script>

</body>
</html>
