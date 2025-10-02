import React, {useEffect, useRef, useState} from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "../../App.css";
import "./Home.css";
import AdCard from "../../components/AdCard/AdCard"; // CSS 따로 관리
import { getHomeContents } from "../../API/HomeApi";
import Footer from "../../components/Footer/Footer";
import ContentSwiper from "../../components/ContentSwiper/ContentSwiper"; // CSS 따로 관리


const Home = () => {
    /* 컨텐츠 상태 */
    const [contents, setContents] = useState(null);
    useEffect(() => {
        console.log(contents);
    }, [contents]);

    /* 홈 컨텐츠 불러오기 */
    useEffect(() => {
        const fetchContents = async () => {
            try {
                setContents(await getHomeContents().then(response => response.data));
            } catch (error) {
                console.error('Error fetching home contents:', error);
            }
        };
        fetchContents();
    }, []);

    /* 도메인 한글 매핑 */
    const domainNameMap = {
        "movie" : "영화",
        "series" : "시리즈",
        "book" : "책",
        "webtoon" : "웹툰",
        "record" : "음반"
    }

    /* 반응형 perView */
    const containerRef = useRef(null);
    const [perView, setPerView] = useState(3);

    useEffect(() => {
        const handleResize = () => {
            if (window.innerWidth < 640) setPerView(1);
            else if (window.innerWidth < 1024) setPerView(2);
            else setPerView(3);
        };
        handleResize();
        window.addEventListener("resize", handleResize);
        return () => window.removeEventListener("resize", handleResize);
    }, []);

    /* 모바일 드래그 */
    let startX = 0;
    let scrollLeft = 0;
    const onTouchStart = e => {
        startX = e.touches[0].pageX;
        scrollLeft = containerRef.current.scrollLeft;
    };
    const onTouchMove = e => {
        const x = e.touches[0].pageX;
        const walk = startX - x;
        containerRef.current.scrollLeft = scrollLeft + walk;
    };

    return (
        <div className="home">
            <div className="home-inner container">

                {/*광고라인*/}
                <div
                    className="ad-card-list"
                    style={{ "--per-view": perView.toString() }} // CSS 변수로 perView 전달
                    ref={containerRef}
                    onTouchStart={onTouchStart}
                    onTouchMove={onTouchMove}
                >
                    <AdCard
                        badge="놓치면 아쉬운 작품"
                        tag="tvN · 로맨스"
                        title="나와 너를 응원하는 <미지의 서울>"
                        subtitle="어제는 끝났고 내일은 멀었고 오늘은 아직 모른다"
                        image="/ad-poster1.jpg"
                        link="/content/2"
                    />

                    <AdCard
                        badge="예술의 스펙트럼"
                        tag="배우 · 감독 · 저자"
                        title="하정우"
                        subtitle="연기와 연출, 글쓰기로 확장된 세계"
                        image="/ad-poster2.jpg"
                        link="/person/30"
                    />

                    <AdCard
                        tag="유저 컬렉션"
                        title="나의 철학"
                        subtitle="개인의 취향이 담긴 작품 모음"
                        image="/ad-poster3.jpg"
                        link="/collection/53"
                    />
                </div>

                {/*컨텐츠 라인*/}
                <div className="home-content">
                    {contents &&
                        Object.entries(contents).map(([category, items], idx) => (
                            <div key={category}>
                                <h2 className="home-content-title">{`잇뷰 최신 TOP 10 ${domainNameMap[category]}`}</h2>
                                <ContentSwiper data={items}  idx={idx}/>
                            </div>
                        ))}
                </div>
            </div>

            <Footer/>

        </div>
    )
}
export default Home