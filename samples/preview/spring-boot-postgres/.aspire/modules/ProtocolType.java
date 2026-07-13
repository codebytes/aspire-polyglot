// ProtocolType.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** ProtocolType enum. */
public enum ProtocolType implements WireValueEnum {
    IP("IP"),
    IPV6_HOP_BY_HOP_OPTIONS("IPv6HopByHopOptions"),
    UNSPECIFIED("Unspecified"),
    ICMP("Icmp"),
    IGMP("Igmp"),
    GGP("Ggp"),
    IPV4("IPv4"),
    TCP("Tcp"),
    PUP("Pup"),
    UDP("Udp"),
    IDP("Idp"),
    IPV6("IPv6"),
    IPV6_ROUTING_HEADER("IPv6RoutingHeader"),
    IPV6_FRAGMENT_HEADER("IPv6FragmentHeader"),
    IPSEC_ENCAPSULATING_SECURITY_PAYLOAD("IPSecEncapsulatingSecurityPayload"),
    IPSEC_AUTHENTICATION_HEADER("IPSecAuthenticationHeader"),
    ICMP_V6("IcmpV6"),
    IPV6_NO_NEXT_HEADER("IPv6NoNextHeader"),
    IPV6_DESTINATION_OPTIONS("IPv6DestinationOptions"),
    ND("ND"),
    RAW("Raw"),
    IPX("Ipx"),
    SPX("Spx"),
    SPX_II("SpxII"),
    UNKNOWN("Unknown");

    private final String value;

    ProtocolType(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static ProtocolType fromValue(String value) {
        for (ProtocolType e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
