package com.gdu.ocean.controller;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gdu.ocean.service.IamPortService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.gdu.ocean.domain.OrderInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
public class IamPortController {

    private final IamPortService iamPortService;
    private IamportClient api;
    @Value("${imp_key}")
	private String impKey;
	
	@Value("{imp_secret}")
	private String impSecret;
	
    private IamPortController() {
        this.iamPortService = null;
		// REST API 키와 REST API secret 를 아래처럼 순서대로 입력한다.
        this.api = new IamportClient(impKey,impSecret);
    }
    @ResponseBody
    @PostMapping(value="/verifyiamport/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(
            Model model
            , Locale locale
            , HttpSession session
            , @PathVariable(value= "imp_uid") String imp_uid) throws IamportResponseException, IOException {
        return api.paymentByImpUid(imp_uid);
	}
}
    
    
    
    /*
    @PostMapping("/record")
    public ResponseEntity<?> paymentRecordGenerateBeforePg(@Valid @RequestBody PaymentRequest paymentRequest, UsersDTO usersNo) {
        PaymentResponse paymentResponse = paymentService.paymentRecordGenerateBeforePg(paymentRequest, usersNo);
        return ResponseEntity.ok(paymentResponse);
    }

    @PostMapping("/complete")
    public ResponseEntity<?> paymentResult(@RequestBody String imp_uid) throws JsonIOException {
        System.out.println("imp_uid : " + imp_uid);

        String token = paymentService.getToken();
        System.out.println("token : " + token);

        return ResponseEntity.ok().build();
    }
    */

