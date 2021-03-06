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

    //type 0x02 authorization
    public static final byte MSG_TYPE_AUTH = 0x02;

    public static final byte MSG_EXTENDED_TYPE_LOGIN_REQUEST = 0x01;
    public static final byte MSG_EXTENDED_TYPE_LOGIN_RESPONSE = 0x02;
    public static final byte MSG_EXTENDED_TYPE_LIST_CHARACTERS_REQUEST = 0x03;
    public static final byte MSG_EXTENDED_TYPE_LIST_CHARACTERS_RESPONSE = 0x04;
    public static final byte MSG_EXTENDED_TYPE_CREATE_CHARACTER_REQUEST = 0x05;
    public static final byte MSG_EXTENDED_TYPE_CREATE_CHARACTER_RESPONSE = 0x06;
    public static final byte MSG_EXTENDED_TYPE_SELECT_CHARACTER_REQUEST = 0x07;
    public static final byte MSG_EXTENDED_TYPE_SELECT_CHARACTER_RESPONSE = 0x08;

    //type 0x06 general client state information (e.q. show loading screen)
    public static final byte MSG_TYPE_GENERAL_CLIENT_STATE_INFORMATION = 0x06;

    public static final byte MSG_EXTENDED_TYPE_LOAD_REGION = 0x01;//shows load region screen on client

    //type 0x0B error
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
    public static final byte MSG_EXTENDED_TYPE_PUBLIC_KEY_REQUEST = 0x02;
    public static final byte MSG_EXTENDED_TYPE_PUBLIC_KEY_RESPONSE = 0x03;

}
