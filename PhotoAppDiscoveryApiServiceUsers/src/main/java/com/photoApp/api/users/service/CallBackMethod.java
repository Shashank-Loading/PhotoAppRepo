package com.photoApp.api.users.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.photoApp.api.users.ui.model.AlbumResponseModel;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@FeignClient(name="albums-ws")
public interface CallBackMethod {
	
	@CircuitBreaker(name="albums-ws", fallbackMethod="getAlbumFallback")
	@GetMapping("/users/{id}/albums")
	public List<AlbumResponseModel>  getAlbums(@PathVariable("id" )String id);
	
	
	default List<AlbumResponseModel> getAlbumFallback(String id, Throwable th){
		System.out.println("+++++++++++++++albums-ws down");
		return new ArrayList<>();
	}

}

//class GetAlbumsFallback  implements CallBackMethod{
//
//	@Override
//	public String getAlbums(String id) {
//		// TODO Auto-generated method stub
//		return "method is down";
//	}
//	
//}
