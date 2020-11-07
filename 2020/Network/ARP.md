# ARP (Address Resolution Protocol)

주소 결정 프로토콜(Address Resolution Protocol, ARP)은 네트워크 상에서 IP 주소를 물리적 네트워크 주소로 대응(bind)시키기 위해 사용되는 프로토콜이다. 여기서 물리적 네트워크 주소는 이더넷 또는 토큰링의 48 비트 네트워크 카드 주소를 뜻한다. ARP는 1982년 인터넷 표준 STD 37인 "RFP 826"에 의해 정의되었다. [위키백과 - 주소 결정 프로토콜](https://ko.wikipedia.org/wiki/%EC%A3%BC%EC%86%8C_%EA%B2%B0%EC%A0%95_%ED%94%84%EB%A1%9C%ED%86%A0%EC%BD%9C)

그러니까 A가 B에게 IP를 이용해서 패킷을 전송하려고 하는데, B의 MAC주소를 모를 때 ARP 를 사용해서 B의 IP 주소와 Broadcast MAC 주소(FFFFFFFFFFFF)를 가지는 ARP 패킷을 네트워크로 쏜다. 그러면 B가 이 패킷을 받고 본인의 MAC 주소를 A에게 돌려준다.
