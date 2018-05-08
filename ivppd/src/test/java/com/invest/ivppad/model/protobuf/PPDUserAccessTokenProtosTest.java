package com.invest.ivppad.model.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import junit.framework.TestCase;

/**
 * Created by xugang on 2017/9/2.do best.
 */
public class PPDUserAccessTokenProtosTest extends TestCase {
    public void testApp() {
        PPDUserAccessTokenProtos.PPDUserAccessToken token =
                PPDUserAccessTokenProtos.PPDUserAccessToken.newBuilder()
                        .setAccessToken("sfdsfdsfsf")
                        .setExpiresIn(121212)
                        .setRefreshToken("sfs222")
                        .build();
        byte[] bytes = token.toByteArray();

        try {
            PPDUserAccessTokenProtos.PPDUserAccessToken token2 = PPDUserAccessTokenProtos.PPDUserAccessToken.parseFrom(bytes);
            System.out.println(token2.getAccessToken());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        assertTrue(true);
    }
}