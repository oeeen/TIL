# IPsec VPN

VPN은 Virtual Private Network, 외부에서 접근할 수 없는 사설망이다. Private Network와의 연결은 가상 터널을 통해 이루어지고, 이 가상 터널은 IPsec(Internet Protocol Security)으로 암호화하여 보호하는 것이 IPsec VPN이다.

## IKE (Internet Key Exchange)

위에서 말한 가상 터널을 만드는 과정에서 사용되는 것이 IKE이다. IPsec을 위한 SA(Security Association)를 만들고 유지하며 그에 따른 키 관리를 수행한다.

IKE는 다른 프로토콜들(ISAKMP, OAKLEY, SKEME 등)을 참조하고, 이런 키 교환 프로토콜들을 이용해서 보안 채널에 필요한 SA를 확립한다.

IKE는 v2가 있는데, IKEv1 보다 더 가볍고 효율적이나 보안성은 강화되었다.(인증방법 - PSK, RSA-Sig, EAP) IKEv1과 IKEv2 간에 호환성 없음.

[정보통신기술용어해설 - IKE](http://www.ktword.co.kr/abbr_view.php?m_temp1=2284)
