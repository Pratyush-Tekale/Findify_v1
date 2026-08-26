<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%
com.findify.model.User loggedInUser =
    (com.findify.model.User) session.getAttribute("loggedInUser");

if (loggedInUser == null) {
    response.sendRedirect("login.jsp");
    return;
}
if (!"ADMIN".equals(loggedInUser.getRole())) {
    response.sendRedirect("login.jsp");
    return;
}
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

  <div class="admin-welcome">
    <div class="welcome-chip">
      <div class="av">${loggedInUser.fullName.substring(0,1)}</div>
      <span>Welcome, ${loggedInUser.fullName}</span>
    </div>
    <p class="section-sub" style="margin:0;">
      Manage campus claims and monitor all lost &amp; found activity.
    </p>
  </div>

  <!-- 6-card stat grid -->
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

  <form action="AdminDashboardServlet" method="get" class="filter-bar">
    <input
      type="text"
      name="search"
      placeholder="Search Item"
      value="${searchValue}">

    <select name="status">
      <option value="" ${statusValue == '' ? 'selected' : ''}>All Status</option>
      <option value="PENDING" ${statusValue == 'PENDING' ? 'selected' : ''}>Pending</option>
      <option value="APPROVED" ${statusValue == 'APPROVED' ? 'selected' : ''}>Approved</option>
      <option value="REJECTED" ${statusValue == 'REJECTED' ? 'selected' : ''}>Rejected</option>
    </select>

    <button type="submit">Search</button>
    <c:if test="${not empty searchValue or not empty statusValue}">
      <a href="AdminDashboardServlet" class="btn btn-reject" style="text-decoration:none;">Clear</a>
    </c:if>
  </form>

  <!-- Two-column layout: claims table + recent activity rail -->
  <div class="dash-columns">

    <div>
      <div class="section-head" id="pendingClaims">
        <h2 class="section-title">${viewLabel}</h2>
        <p class="section-sub">Review and verify all submitted claim requests.</p>
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
            <c:choose>
              <c:when test="${empty pendingClaims}">
                <tr>
                  <td colspan="8" style="text-align:center;">No Claims Found</td>
                </tr>
              </c:when>
              <c:otherwise>
                <c:forEach var="claim" items="${pendingClaims}">
                  <tr>
                    <td><span class="id-chip">#${claim.claimId}</span></td>
                    <td>${claim.itemName}</td>
                    <td>${claim.claimantName}</td>
                    <td>${claim.claimantPhone}</td>
                     <td>
    <div class="trust-bar">
        <div class="track">
            <div
                class="fill ${claim.matchPercentage >= 75 ? 'high' : claim.matchPercentage >= 40 ? 'mid' : 'low'}"
                style="width:${claim.matchPercentage}%;">
            </div>
        </div>

        <span>${claim.matchedAnswers}/${claim.totalQuestions}</span>
    </div>
</td>
                    <td><fmt:formatDate value="${claim.claimDate}" pattern="dd MMM yyyy HH:mm"/></td>
                    <td>
                      <c:choose>
                        <c:when test="${claim.status=='PENDING'}">
                          <span class="badge badge-amber">Pending</span>
                        </c:when>
                        <c:when test="${claim.status=='APPROVED'}">
                          <span class="badge badge-green">Approved</span>
                        </c:when>
                        <c:otherwise>
                          <span class="badge badge-red">Rejected</span>
                        </c:otherwise>
                      </c:choose>
                    </td>
                    <td>
                      <button type="button" class="btn btn-view"
                        data-id="${claim.claimId}"
                        data-item="${fn:escapeXml(claim.itemName)}"
                        data-found-id="${claim.foundId}"
                        data-desc="${fn:escapeXml(claim.itemDescription)}"
                        data-location="${fn:escapeXml(claim.locationFound)}"
                        data-date-found="<fmt:formatDate value="${claim.dateFound}" pattern="dd MMM yyyy"/>"
                        data-image="${fn:escapeXml(claim.itemImage)}"
                        data-image-base="${pageContext.request.contextPath}/uploads/"
                        data-claimant="${fn:escapeXml(claim.claimantName)}"
                        data-claimant-id="${claim.claimantId}"
                        data-phone="${claim.claimantPhone}"
                        data-matched="${claim.matchedAnswers}"
                        data-total="${claim.totalQuestions}"
                        data-match-pct="${claim.matchPercentage}"
                        data-qa-id="qa-${claim.claimId}"
                        data-status="${claim.status}"
                        data-date="<fmt:formatDate value="${claim.claimDate}" pattern="dd MMM yyyy, HH:mm"/>"
                        onclick="openClaimModal(this.dataset)">View</button>
                      <div id="qa-${claim.claimId}" style="display:none;">
                        <c:forEach var="ans" items="${claim.answers}">
                          <div class="qa-item">
                            <strong>${fn:escapeXml(ans.questionText)}</strong>
                            <div>Correct answer: ${fn:escapeXml(ans.correctAnswer)}</div>
                            <div>Submitted: ${fn:escapeXml(ans.submittedAnswer)}
                              ${ans.correct ? '&#9989;' : '&#10060;'}</div>
                          </div>
                        </c:forEach>
                      </div>
                      <c:if test="${claim.status=='PENDING'}">
                        <form action="ManageClaimsServlet" method="post" style="display:inline;">
                          <input type="hidden" name="claimId" value="${claim.claimId}">
                          <input type="hidden" name="action" value="approve">
                          <button class="btn btn-approve">Approve</button>
                        </form>
                        <form action="ManageClaimsServlet" method="post" style="display:inline;">
                          <input type="hidden" name="claimId" value="${claim.claimId}">
                          <input type="hidden" name="action" value="reject">
                          <button class="btn btn-reject">Reject</button>
                        </form>
                      </c:if>
                      <c:if test="${claim.status!='PENDING'}">
                        <span class="completed">Completed</span>
                      </c:if>
                    </td>
                  </tr>
                </c:forEach>
              </c:otherwise>
            </c:choose>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Recent Activity rail -->
    <div class="activity-panel">
      <h3>Recent Activity</h3>
      <ul class="activity-list">
        <c:choose>
          <c:when test="${empty recentActivity}">
            <li>Nothing yet.</li>
          </c:when>
          <c:otherwise>
            <c:forEach var="a" items="${recentActivity}">
              <li>
                <span class="dot
                  <c:choose>
                    <c:when test="${a.status=='APPROVED'}">green</c:when>
                    <c:when test="${a.status=='REJECTED'}">red</c:when>
                    <c:otherwise>blue</c:otherwise>
                  </c:choose>
                "></span>
                <div>
                  <div>
                    <strong>${a.claimantName}</strong> claimed
                    <strong>${a.itemName}</strong> —
                    <c:choose>
                      <c:when test="${a.status=='APPROVED'}">approved</c:when>
                      <c:when test="${a.status=='REJECTED'}">rejected</c:when>
                      <c:otherwise>pending review</c:otherwise>
                    </c:choose>
                  </div>
                  <span class="a-time">
                    <fmt:formatDate value="${a.claimDate}" pattern="dd MMM, HH:mm"/>
                  </span>
                </div>
              </li>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </ul>
    </div>

  </div>
</div>
</section>

<footer class="site-footer">
  <div class="wrap">
    <div class="footer-bottom">
      <span>© 2026 FINDIFY</span>
      <span>Campus Lost &amp; Found Admin Panel</span>
    </div>
  </div>
</footer>

<!-- Claim Detail Modal -->
<div class="modal-overlay" id="claimModalOverlay" onclick="if(event.target===this) closeClaimModal()">
  <div class="modal-card">
    <div class="modal-head">
      <h3>Claim <span id="mClaimId"></span></h3>
      <button type="button" class="modal-close" onclick="closeClaimModal()">&times;</button>
    </div>

    <div class="modal-body">
      <div class="modal-row">
        <span class="modal-label">Status</span>
        <span id="mStatus"></span>
      </div>
      <div class="modal-row">
        <span class="modal-label">Verification Score</span>
        <span id="mTrust"></span>
      </div>
      <div id="mImageWrap" class="modal-row full" style="display:none;">
        <span class="modal-label">Item Photo</span>
        <img id="mImage" class="modal-image" alt="Item photo">
      </div>
      <div class="modal-row">
        <span class="modal-label">Item Claimed</span>
        <span id="mItem"></span>
      </div>
      <div class="modal-row full">
        <span class="modal-label">Item Description</span>
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
        <span class="modal-label">Verification Q&amp;A</span>
        <div id="mProof" class="modal-proof"></div>
      </div>
    </div>
  </div>
</div>

<script>
function openClaimModal(c) {
  document.getElementById('mClaimId').textContent = '#' + c.id;
  document.getElementById('mItem').textContent = c.item || '—';
  document.getElementById('mDesc').textContent = c.desc && c.desc !== 'null' ? c.desc : 'No description on file.';
  document.getElementById('mLocation').textContent = c.location && c.location !== 'null' ? c.location : '—';
  document.getElementById('mDateFound').textContent = c.dateFound || '—';
  document.getElementById('mFoundId').textContent = 'found_id: ' + c.foundId;
  document.getElementById('mClaimant').textContent = c.claimant || '—';
  document.getElementById('mClaimantId').textContent = 'user_id: ' + c.claimantId;
  document.getElementById('mPhone').textContent = c.phone || '—';
  document.getElementById('mDate').textContent = c.date || '—';

  var qaSource = document.getElementById(c.qaId);
  document.getElementById('mProof').innerHTML =
    qaSource && qaSource.innerHTML.trim() !== '' ? qaSource.innerHTML : '<p>No verification questions on file.</p>';

  var imgWrap = document.getElementById('mImageWrap');
  var img = document.getElementById('mImage');
  if (c.image && c.image !== 'null' && c.image.trim() !== '') {
    img.src = c.imageBase + encodeURIComponent(c.image);
    imgWrap.style.display = 'flex';
  } else {
    imgWrap.style.display = 'none';
  }

  var pct = Number(c.matchPct || 0);
  document.getElementById('mTrust').innerHTML =
    '<span class="trust-pill ' + (pct >= 75 ? 'high' : pct >= 40 ? 'mid' : 'low') + '">' +
    c.matched + ' / ' + c.total + ' questions (' + pct + '%)</span>';

  var statusHtml = c.status === 'PENDING' ? '<span class="badge badge-amber">Pending</span>'
    : c.status === 'APPROVED' ? '<span class="badge badge-green">Approved</span>'
    : '<span class="badge badge-red">Rejected</span>';
  document.getElementById('mStatus').innerHTML = statusHtml;

  document.getElementById('claimModalOverlay').classList.add('open');
}
function closeClaimModal() {
  document.getElementById('claimModalOverlay').classList.remove('open');
}
document.addEventListener('keydown', function(e) {
  if (e.key === 'Escape') closeClaimModal();
});
</script>

</body>
</html>
