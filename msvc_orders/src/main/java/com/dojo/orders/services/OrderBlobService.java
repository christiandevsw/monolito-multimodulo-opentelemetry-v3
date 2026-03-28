package com.dojo.orders.services;

import com.azure.storage.blob.BlobClient;

import java.util.List;
import java.util.Optional;

public interface OrderBlobService {
    //    ResponseEntity<byte[]> getBytesBlob(String name);
    List<String> listBlobNames();
    Optional<BlobClient>  findFileBlob(String name);
}
