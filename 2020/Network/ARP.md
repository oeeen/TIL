# ARP (Address Resolution Protocol)

주소 결정 프로토콜(Address Resolution Protocol, ARP)은 네트워크 상에서 IP 주소를 물리적 네트워크 주소로 대응(bind)시키기 위해 사용되는 프로토콜이다. 여기서 물리적 네트워크 주소는 이더넷 또는 토큰링의 48 비트 네트워크 카드 주소를 뜻한다. ARP는 1982년 인터넷 표준 STD 37인 "RFP 826"에 의해 정의되었다. [위키백과 - 주소 결정 프로토콜](https://ko.wikipedia.org/wiki/%EC%A3%BC%EC%86%8C_%EA%B2%B0%EC%A0%95_%ED%94%84%EB%A1%9C%ED%86%A0%EC%BD%9C)

그러니까 A가 B에게 IP를 이용해서 패킷을 전송하려고 하는데, B의 MAC주소를 모를 때 ARP 를 사용해서 B의 IP 주소와 Broadcast MAC 주소(FFFFFFFFFFFF)를 가지는 ARP 패킷을 네트워크로 쏜다. 그러면 B가 이 패킷을 받고 본인의 MAC 주소를 A에게 돌려준다.

> IP를 이용해서 해당 IP를 사용하는 단말의 MAC주소를 찾는 프로토콜

일단 `arp -a` 명령을 사용해보자.

![arp -a](/2020/assets/img/arp.png)

WireShark를 이용해서 잡히는 ARP 패킷을 봤다.

![wireshark arp broadcast](/2020/assets/img/arp_wireshark_broadcast.png)

Destination이 FFFFFFFFFFFF로 Broadcast임을 알 수 있다. 그리고 172.30.1.36(private 대역) IP를 가진 단말의 MAC주소를 찾는 요청임을 알 수 있다. 이 외에도 ARP 요청만 filtering 해서 보고 있는데, broadcast가 아닌 특정 MAC으로 보내는 요청이 많았고, 그에 대한 응답도 바로 따라왔다.

![wireshark arp](/2020/assets/img/arp_wireshark.png)

이 요청은 8c:85:90:c5:b1:41는 내 맥북의 MAC 주소이고 이 맥북에게 172.30.1.31이 누구냐고 물어봤다. 그래서 내 맥북은 8c:85:90:c5:b1:41라고 응답을 보낸 것이다. 근데 이걸 왜 지속적으로 요청하고 응답하는지는 모르겠다..
