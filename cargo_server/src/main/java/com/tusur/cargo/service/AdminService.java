package com.tusur.cargo.service;

import com.tusur.cargo.dto.AdminRequest;
import com.tusur.cargo.dto.AdminResponse;
import java.util.List;

public interface AdminService {

  short createAdmin(AdminRequest adminRequest);

  List<AdminResponse> getAllAdmin();
}
