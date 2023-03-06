# 🎬 영화검색하기
영화를 검색해보자 :)

## 🤔 프로젝트 설명

> Naver 검색 API를 활용한 영화 검색 앱입니다!

<br>

### 💻 기술스택 
#### ▪️ Client
<p>
 <img src="https://img.shields.io/badge/Anroid-3DDC84?style=for-the-badge&logo=Android&logoColor=white">
 <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white">
 <img src="https://img.shields.io/badge/Retrofit2-3E4348?style=for-the-badge&logo=Square&logoColor=white">
 <img src="https://img.shields.io/badge/OkHttp-3E4348?style=for-the-badge&logo=Square&logoColor=white">
 <img src="https://img.shields.io/badge/Room-003B57?style=for-the-badge&logo=SQLite&logoColor=white">
 <img src="https://img.shields.io/badge/MVVM-3DDC84?style=for-the-badge&logo=&logoColor=white">
 <img src="https://img.shields.io/badge/Coroutine-3DDC84?style=for-the-badge&logo=&logoColor=white">
 <img src="https://img.shields.io/badge/DataBinding-0F9D58?style=for-the-badge&logo=&logoColor=white">
 <img src="https://img.shields.io/badge/Hilt-0F9D58?style=for-the-badge&logo=&logoColor=white">
 <img src="https://img.shields.io/badge/Glide-0F9D58?style=for-the-badge&logo=&logoColor=white">
</p>

#### ▪️ Server
<p>
 <img src="https://img.shields.io/badge/NaverAPI-03C75A?style=for-the-badge&logo=Naver&logoColor=white">
</p>
<br>

### 🛠 구현 사항
##### 1️⃣ 영화 검색하기
###### Rest 통신을 통해 Naver API에서 키워드에 따른 영화 목록을 표시해줍니다.

##### 2️⃣ 영화 상세정보
###### 전달받은 Response의 URL을 받아 WebView를 통해 해당 영화의 상세정보를 확인 할 수 있습니다.
 
##### 3️⃣ 최근 검색어 저장
###### Room을 통해 사용자가 최근 검색한 검색어를 보여주는 페이지로 최근 10개까지 목록을 보여줍니다. 뿐만 아니라 전체 삭제를 통해 모든 최근 기록을 삭제 할 수 있습니다.

<br>

### 😎 프로젝트 사용기술 설명
##### 1️⃣ Dagger Hilt를 활용하여 의존성을 주입 해주었습니다.
##### 2️⃣ MVVM 디자인 기반으로 프로젝트를 진행 하였습니다.
##### 3️⃣ Coroutine을 통한 비동기 처리를 하였습니다.
##### 4️⃣ Retrofit2를 통해 Rest통신을 하였습니다.
##### 5️⃣ Room을 활용하여 내부 저장소에 최근 검색어 정보를 저장하도록 구현 하였습니다
##### 6️⃣ Glide를 통해 이미지 작업을 진행 하였습니다.
##### 7️⃣ Repository를 사용하여 Data를 관리 하였습니다.
##### 8️⃣ ListAdapter , DiffUtil , AsyncListDiffer를 사용하여 RecyclerView의 Adapter를 구현 하였습니다.
##### 9️⃣ WebView를 통해 영화 상세정보를 표시하도록 구현 하였습니다.

<br>

### 🎥 시연 화면
<div align="center">
 <img width="30%" alt="app_main" src="https://user-images.githubusercontent.com/65700842/223024393-02bc3642-1e64-46ef-a8c1-44d60aceed7e.gif">
</div>

