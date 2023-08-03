package com.crking7.datn.web.dto.response;

import lombok.Data;

@Data
public class BannerResponse {
	private long id;

	private String name;

	private String src;

	private String msg;

	private int status;

	private long categoryId;
}
