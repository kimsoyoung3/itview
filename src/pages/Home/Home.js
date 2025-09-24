import React, { useEffect } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "../../App.css";
import "./Home.css";
import AdCard from "../../components/AdCard/AdCard"; // CSS 따로 관리
import { getHomeContents } from "../../API/HomeApi";
import Footer from "../../components/Footer/Footer"; // CSS 따로 관리


const Home = () => {

    const [contents, setContents] = React.useState(null);
    useEffect(() => {
        console.log(contents);
    }, [contents]);

    useEffect(() => {
        const fetchContents = async () => {
            try {
                let data = {};
                data['MOVIE'] = await getHomeContents('MOVIE', 1).then(response => response.data);
                data['SERIES'] = await getHomeContents('SERIES', 1).then(response => response.data);
                data['BOOK'] = await getHomeContents('BOOK', 1).then(response => response.data);
                data['WEBTOON'] = await getHomeContents('WEBTOON', 1).then(response => response.data);
                data['RECORD'] = await getHomeContents('RECORD', 1).then(response => response.data);
                setContents(data);
            } catch (error) {
                console.error('Error fetching home contents:', error);
            }
        };
        fetchContents();
    }, []);

    return (
        <div className="home">
            <div className="home-inner container">
                {/*배너*/}
                <div className="banner">
                    <img src={`${process.env.PUBLIC_URL}/banner.jpg`} alt=""/>
                </div>

                {/*광고라인*/}
                <div style={{ display: 'flex', gap: '20px', marginTop: '20px' }}>
                    <AdCard
                        badge="인기 아티클"
                        tag="🎬 아티클"
                        title="‘존 윅’ 유니버스 신작 (발레리나) 예습하기"
                        subtitle="또다시 확장되는 암살자들의 세계 완벽 정리 🔥"
                        image="/image.jpg"
                        link="https://watcha.com/article/1"
                    />

                    <AdCard
                        badge="지금 가장 핫한 작품"
                        tag="tvN · 로맨스"
                        title="서초동"
                        subtitle="평균 ★3.1"
                        image="/images/seochodong.jpg"
                        link="https://watcha.com/work/2"
                    />

                    <AdCard
                        tag="신작 스릴러"
                        title="이병헌X손예진 <어쩔수가없다>"
                        subtitle="9월 대개봉 기대작"
                        image="/images/drama.jpg"
                        link="https://watcha.com/trailer/3"
                    />
                </div>

                {/*컨텐츠 라인*/}
                <div className="home-content">
                    <h2 className="home-content-title">
                        잇뷰 영화
                    </h2>
                </div>
            </div>
            <div>
                <Footer/>
            </div>
        </div>
    )
}
export default Home