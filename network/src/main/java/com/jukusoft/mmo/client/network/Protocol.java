package com.jukusoft.mmo.client.network;

public class Protocol {

    protected Protocol() {
        //
    }

    /**
     * protocol information
     */
    public static final int MSG_HEADER_LENGTH = 8;//header length in bytes
    public static final int MSG_HEADER_CID_POS = 4;
    public static final int MSG_BODY_OFFSET = 8;
    public static final short MSG_PROTOCOL_VERSION = 1;

    /**
     * protocol types
     */

    //message types
    public static final byte MSG_TYPE_GS = 0x01;
    public static final byte MSG_TYPE_AUTH = 0x02;

    public static final byte MSG_TYPE_ERROR = 0x0B;

    //type 0x0B error messages & hints
    public static final byte MSG_EXTENDED_TYPE_INCOMPATIBLE_CLIENT = 0x01;
    public static final byte MSG_EXTENDED_TYPE_INTERNAL_SERVER_ERROR = 0x02;

    /**
    * client - proxy messages
     *
     * type: 0x0C
    */
    public static final byte MSG_TYPE_PROXY = 0x0C;

    public static final byte MSG_EXTENDED_TYPE_RTT = 0x01;

}
