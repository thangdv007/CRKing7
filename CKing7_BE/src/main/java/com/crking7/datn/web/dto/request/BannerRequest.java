package com.crking7.datn.web.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class BannerRequest {

	private String name;

	private String src;

	private long categoryId;
}
