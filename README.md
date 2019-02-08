[![Build Status](https://travis-ci.com/jaseyrae9/ISA.svg?token=LfU8WskipJPYJ3mUzo1g&branch=master)](https://travis-ci.com/jaseyrae9/ISA)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/eee53e4eb8734553ba79bc1bdca47fd6)](https://www.codacy.com?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jaseyrae9/ISA&amp;utm_campaign=Badge_Grade)
[![codecov](https://codecov.io/gh/jaseyrae9/ISA/branch/master/graph/badge.svg?token=QIB9smWDwq)](https://codecov.io/gh/jaseyrae9/ISA)
# Projekat iz predmeta ISA 2018/2019
Članovi tima: Jelena Šurlan 7/2015, Milica Todrović 17/2015, Miloš Krstić 107/2015 <br/>
Backend: https://isa-back.herokuapp.com <br/>
Frontend: https://ticket-reservation21.herokuapp.com <br/>

Napomena: Trenutno deployovana verzija backend-a kao dozvoljene cross origine ima:
<ul>
  <li> https://ticket-reservation21.herokuapp.com  i http://ticket-reservation21.herokuapp.com </li>
  <li> localhost://8080 i localhost://4200
</ul>

Za izradu projekta korišteni su: 
<ul>
  <li> Spring Boot </li>
  <li> Postgresql baza </li>
  <li> Angular 6 </li>
</ul>
Korištena radna okruženja: Eclipse za server-app, Visual Studio Code za client-app.
Pokretanje back dela - folder server-app:
<ul>
  <li> U lokalu kreirati Postgresql bazu podataka sa nazivom isa. </li>  
  <li> Zameniti trenutni sadržaj aplication.properties fajla sa sledećim:
    <pre>
      spring.datasource.url = jdbc:postgresql://localhost:5432/isa
      spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults = false
      spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQL95Dialect
      spring.datasource.username = username
      spring.datasource.password = sifra
      spring.jpa.hibernate.ddl-auto = create-drop
      spring.jpa.show-sql=false
      spring.datasource.initialization-mode=always</pre> </li>
  <li> Uraditi maven update projekta </li>
  <li> Projekat se pokreće na portu 8080. </li>
</ul>

Pokretanje front dela - folder client-app:
<ul>
  <li> Skinuti sve potrebne dependency pomoću npm update. </li>
  <li> Pokrenuti aplikaciju sa ng serve. </li>
  <li> Projekat se pokreće na portu 4200. Trenutno je konfigurisan tako da gađa deploy verziju backend-a. </li>
  <li> Detaljne informacije o pokretanju client-app mogu se pronaći u okviru readme fajla u datom folderu. </li>
</ul> 

