package org.superbiz.moviefun.albumsapi;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;

public class AlbumsClient {

    private String albumsUrl;
    private RestOperations restOperations;

    private static ParameterizedTypeReference<List<AlbumInfo>> albumsListType = new ParameterizedTypeReference<List<AlbumInfo>>() {
    };

    public AlbumsClient(String albumsUrl, RestOperations restOperations) {
        this.albumsUrl = albumsUrl;
        this.restOperations = restOperations;
    }

    public List<AlbumInfo> getAlbums() {
        return restOperations.exchange(albumsUrl, GET, null, albumsListType).getBody();
    }

    public AlbumInfo getById(long id) {
        return restOperations.getForObject(albumsUrl + "/" + id, AlbumInfo.class);
    }

    public void add(AlbumInfo album) {
        restOperations.postForObject(albumsUrl, album, AlbumInfo.class);
    }

    public HttpEntity<byte[]> getCover(long id) {
//        return restOperations.execute(albumsUrl + "/" + id, HttpMethod.GET, null, response -> IOUtils.toByteArray(response.getBody()));
        return restOperations.getForEntity(albumsUrl + "/" + id, byte[].class);
    }

    public void uploadCover(long id, byte[] bytes) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", bytes);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        restOperations.postForObject(albumsUrl + "/" + id, requestEntity, Void.class);
    }

}