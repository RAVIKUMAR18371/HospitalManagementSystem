// ===================== CONFIG =====================
const BASE_URL = 'http://localhost:8099';
let currentUser = null; // { email, password, role }

// ===================== NAVIGATION =====================
function showPage(pageId) {
  document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
  document.getElementById(pageId).classList.add('active');
  window.scrollTo(0, 0);
}

function switchTab(show, hide, btn) {
  document.getElementById(show).classList.add('active');
  document.getElementById(hide).classList.remove('active');
  btn.closest('.tabs').querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
  btn.classList.add('active');
}

// ===================== HELPERS =====================
function makeHeaders(contentType = 'application/json') {
  const headers = { 'Content-Type': contentType };
  if (currentUser) {
    headers['Authorization'] = 'Basic ' + btoa(currentUser.email + ':' + currentUser.password);
  }
  return headers;
}

function showAlert(id, msg, type) {
  const el = document.getElementById(id);
  el.className = `alert alert-${type} show`;
  const icon = type === 'success' ? '✅' : type === 'error' ? '❌' : 'ℹ️';
  el.innerHTML = icon + ' ' + msg;
}

function quickFill(email, pw) {
  document.getElementById('loginEmail').value = email;
  document.getElementById('loginPassword').value = pw;
}

function logout() {
  currentUser = null;
  document.getElementById('userBadge').classList.remove('show');
  document.getElementById('logoutBtn').style.display = 'none';
  showPage('page-home');
}

function setBtn(btnId, loading, defaultText) {
  const btn = document.getElementById(btnId);
  btn.disabled = loading;
  btn.innerHTML = loading
    ? '<span class="spinner"></span> Please wait...'
    : defaultText;
}

// ===================== LOGIN =====================
async function doLogin() {
  const email    = document.getElementById('loginEmail').value.trim();
  const password = document.getElementById('loginPassword').value.trim();

  if (!email || !password) {
    showAlert('loginAlert', 'Please enter email and password', 'error');
    return;
  }

  setBtn('loginBtn', true, 'Login');

  try {
    const res = await fetch(
      `${BASE_URL}/api/auth/login?email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`,
      { method: 'POST' }
    );

    const text = await res.text();

    if (res.ok && text.includes('successful')) {
      currentUser = { email, password };

      // Detect role automatically
      const role = await detectRole(email, password);
      currentUser.role = role;

      // Update navbar
      document.getElementById('badgeText').textContent = email + ' (' + role + ')';
      document.getElementById('userBadge').classList.add('show');
      document.getElementById('logoutBtn').style.display = 'block';

      // Go to correct dashboard
      if      (role === 'SUPER_ADMIN') showPage('page-superadmin');
      else if (role === 'ADMIN')       showPage('page-admin');
      else if (role === 'DOCTOR')      showPage('page-doctor');
      else                             showPage('page-patient');

    } else {
      showAlert('loginAlert', 'Wrong email or password — 401 Unauthorized', 'error');
    }

  } catch (e) {
    showAlert('loginAlert', 'Cannot connect to server. Is Spring Boot running on port 8099?', 'error');
  }

  setBtn('loginBtn', false, 'Login');
}

// Detect role by hitting protected endpoints
async function detectRole(email, password) {
  const headers = { 'Authorization': 'Basic ' + btoa(email + ':' + password) };

  // Try SUPER_ADMIN (delete endpoint)
  const sa = await fetch(`${BASE_URL}/api/admin/patients/delete/0`, { method: 'DELETE', headers });
  if (sa.status !== 403 && sa.status !== 401) return 'SUPER_ADMIN';

  // Try ADMIN (update endpoint)
  const adm = await fetch(`${BASE_URL}/api/admin/patients/update/0`, {
    method: 'PUT',
    headers: { ...headers, 'Content-Type': 'application/json' },
    body: '{}'
  });
  if (adm.status !== 403 && adm.status !== 401) return 'ADMIN';

  // Try DOCTOR (submit-report endpoint)
  const doc = await fetch(
    `${BASE_URL}/api/doctors/submit-report?patientId=0&doctorId=0&department=x&description=x`,
    { method: 'POST', headers: { ...headers, 'Content-Type': 'text/plain' }, body: 'x' }
  );
  if (doc.status !== 403 && doc.status !== 401) return 'DOCTOR';

  return 'USER';
}

// ===================== REGISTER =====================
async function doRegister() {
  const body = {
    name:     document.getElementById('regName').value.trim(),
    email:    document.getElementById('regEmail').value.trim(),
    password: document.getElementById('regPassword').value.trim(),
    age:      parseInt(document.getElementById('regAge').value),
    address:  document.getElementById('regAddress').value.trim(),
    secretId: document.getElementById('regSecretId').value.trim()
  };

  if (!body.name || !body.email || !body.password || !body.secretId) {
    showAlert('registerAlert', 'Please fill all required fields', 'error');
    return;
  }

  setBtn('registerBtn', true, 'Register');

  try {
    const res = await fetch(`${BASE_URL}/api/register/patient`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    });

    const text = await res.text();

    if (res.ok) {
      showAlert('registerAlert', text + ' You can login after admin approval.', 'success');
      // Clear form
      ['regName','regEmail','regPassword','regAge','regAddress','regSecretId']
        .forEach(id => document.getElementById(id).value = '');
    } else {
      showAlert('registerAlert', text || 'Registration failed', 'error');
    }

  } catch (e) {
    showAlert('registerAlert', 'Cannot connect to server. Is Spring Boot running?', 'error');
  }

  setBtn('registerBtn', false, 'Register');
}

// ===================== PATIENT: LOAD RECORDS =====================
async function loadRecords() {
  const pid = document.getElementById('patientIdInput').value;
  if (!pid) { alert('Enter Patient ID first!'); return; }

  const tableDiv = document.getElementById('recordsTable');
  tableDiv.innerHTML = `
    <div class="empty">
      <div class="empty-icon">
        <span class="spinner" style="width:30px;height:30px;border-color:var(--border);border-top-color:var(--accent)"></span>
      </div>
      <p>Loading records...</p>
    </div>`;

  try {
    const res = await fetch(`${BASE_URL}/api/patient-dashboard/${pid}/approved-list`, {
      headers: makeHeaders()
    });

    const data = await res.json();

    if (!data || data.length === 0) {
      tableDiv.innerHTML = `
        <div class="empty">
          <div class="empty-icon">📋</div>
          <p>No medical records found for Patient ID ${pid}</p>
        </div>`;
      return;
    }

    let html = `<table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Patient ID</th>
          <th>Doctor ID</th>
          <th>Department</th>
          <th>Description</th>
          <th>Status</th>
        </tr>
      </thead>
      <tbody>`;

    data.forEach(r => {
      html += `<tr>
        <td>${r.id        || '-'}</td>
        <td>${r.patientId || '-'}</td>
        <td>${r.doctorId  || '-'}</td>
        <td>${r.department|| '-'}</td>
        <td>${r.description||'-'}</td>
        <td><span class="badge badge-approved">Approved</span></td>
      </tr>`;
    });

    html += '</tbody></table>';
    tableDiv.innerHTML = html;

  } catch (e) {
    tableDiv.innerHTML = `
      <div class="empty">
        <div class="empty-icon">⚠️</div>
        <p>Error loading records. Check your login and server connection.</p>
      </div>`;
  }
}

// ===================== PATIENT: DECRYPT =====================
async function doDecrypt() {
  const docId = document.getElementById('decryptDocId').value;
  const key   = document.getElementById('decryptKey').value.trim();

  if (!docId || !key) {
    showAlert('decryptAlert', 'Please enter Document ID and File Key', 'error');
    return;
  }

  try {
    const res = await fetch(
      `${BASE_URL}/api/patient-dashboard/download?documentId=${docId}&enteredFileKey=${encodeURIComponent(key)}`,
      { method: 'POST', headers: makeHeaders() }
    );
    const text = await res.text();

    if (res.ok) {
      showAlert('decryptAlert', text, 'success');
    } else {
      showAlert('decryptAlert', text || 'Decryption failed. Wrong key?', 'error');
    }

  } catch (e) {
    showAlert('decryptAlert', 'Server connection error', 'error');
  }
}

// ===================== ADMIN: UPDATE PATIENT =====================
async function doUpdatePatient() {
  const pid = document.getElementById('updatePatientId').value;

  if (!pid) {
    showAlert('updateAlert', 'Enter Patient ID', 'error');
    return;
  }

  const body = {
    age:      parseInt(document.getElementById('updateAge').value)     || null,
    address:  document.getElementById('updateAddress').value.trim()    || null,
    secretId: document.getElementById('updateSecretId').value.trim()   || null
  };

  try {
    const res = await fetch(`${BASE_URL}/api/admin/patients/update/${pid}`, {
      method: 'PUT',
      headers: makeHeaders(),
      body: JSON.stringify(body)
    });
    const text = await res.text();

    if (res.ok) {
      showAlert('updateAlert', text, 'success');
    } else if (res.status === 403) {
      showAlert('updateAlert', '403 Forbidden — Only Admin can update patients', 'error');
    } else {
      showAlert('updateAlert', text || 'Update failed', 'error');
    }

  } catch (e) {
    showAlert('updateAlert', 'Server connection error', 'error');
  }
}

// ===================== ADMIN: CREATE STAFF =====================
async function doCreateStaff() {
  const type     = document.getElementById('staffType').value;
  const email    = document.getElementById('staffEmail').value.trim();
  const password = document.getElementById('staffPassword').value.trim();

  if (!email || !password) {
    showAlert('staffAlert', 'Fill email and password', 'error');
    return;
  }

  const endpoint = type === 'admin'
    ? '/api/management/create-admin'
    : '/api/management/create-doctor';

  try {
    const res = await fetch(`${BASE_URL}${endpoint}`, {
      method: 'POST',
      headers: makeHeaders(),
      body: JSON.stringify({ email, password })
    });
    const text = await res.text();

    if (res.ok) {
      showAlert('staffAlert', text, 'success');
    } else if (res.status === 403) {
      showAlert('staffAlert', '403 Forbidden — You need higher privileges for this action', 'error');
    } else {
      showAlert('staffAlert', text || 'Failed to create account', 'error');
    }

  } catch (e) {
    showAlert('staffAlert', 'Server connection error', 'error');
  }
}

// ===================== DOCTOR: SUBMIT REPORT =====================
async function doSubmitReport() {
  const patientId    = document.getElementById('docPatientId').value;
  const doctorId     = document.getElementById('docDoctorId').value;
  const dept         = document.getElementById('docDept').value.trim();
  const desc         = document.getElementById('docDesc').value.trim();
  const prescription = document.getElementById('docPrescription').value.trim();

  if (!patientId || !doctorId || !dept || !prescription) {
    showAlert('reportAlert', 'Please fill all fields', 'error');
    return;
  }

  try {
    const url = `${BASE_URL}/api/doctors/submit-report` +
      `?patientId=${patientId}` +
      `&doctorId=${doctorId}` +
      `&department=${encodeURIComponent(dept)}` +
      `&description=${encodeURIComponent(desc)}`;

    const res = await fetch(url, {
      method: 'POST',
      headers: makeHeaders('text/plain'),
      body: prescription
    });

    const text = await res.text();

    if (res.ok) {
      showAlert('reportAlert', 'Report submitted and encrypted successfully!', 'success');

      // Extract file key if present in response
      const keyMatch = text.match(/File Key[:\s]+([a-zA-Z0-9\-]+)/i);
      if (keyMatch) {
        document.getElementById('fileKeyValue').textContent = keyMatch[1];
        document.getElementById('fileKeyBox').classList.add('show');
      }

    } else if (res.status === 403) {
      showAlert('reportAlert', '403 Forbidden — Only Doctors can submit reports', 'error');
    } else {
      showAlert('reportAlert', text || 'Submission failed', 'error');
    }

  } catch (e) {
    showAlert('reportAlert', 'Server connection error', 'error');
  }
}

// ===================== SUPERADMIN: DELETE PATIENT =====================
async function doDeletePatient() {
  const pid = document.getElementById('deletePatientId').value;

  if (!pid) {
    showAlert('deleteAlert', 'Enter Patient ID', 'error');
    return;
  }

  const confirmed = confirm(`⚠️ Are you sure you want to DELETE Patient ID ${pid}?\n\nThis action CANNOT be undone!`);
  if (!confirmed) return;

  try {
    const res = await fetch(`${BASE_URL}/api/admin/patients/delete/${pid}`, {
      method: 'DELETE',
      headers: makeHeaders()
    });
    const text = await res.text();

    if (res.ok) {
      showAlert('deleteAlert', text, 'success');
    } else if (res.status === 403) {
      showAlert('deleteAlert', '403 Forbidden — Only SuperAdmin can delete patients', 'error');
    } else {
      showAlert('deleteAlert', text || 'Delete failed', 'error');
    }

  } catch (e) {
    showAlert('deleteAlert', 'Server connection error', 'error');
  }
}

// ===================== SUPERADMIN: CREATE ADMIN =====================
async function doCreateAdmin() {
  const email    = document.getElementById('saAdminEmail').value.trim();
  const password = document.getElementById('saAdminPassword').value.trim();

  if (!email || !password) {
    showAlert('saAlert', 'Fill all fields', 'error');
    return;
  }

  try {
    const res = await fetch(`${BASE_URL}/api/management/create-admin`, {
      method: 'POST',
      headers: makeHeaders(),
      body: JSON.stringify({ email, password })
    });
    const text = await res.text();

    if (res.ok) {
      showAlert('saAlert', text, 'success');
    } else if (res.status === 403) {
      showAlert('saAlert', '403 Forbidden — Only SuperAdmin can create admins', 'error');
    } else {
      showAlert('saAlert', text || 'Failed to create admin', 'error');
    }

  } catch (e) {
    showAlert('saAlert', 'Server connection error', 'error');
  }
}
