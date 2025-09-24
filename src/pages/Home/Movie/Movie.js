import React, { useEffect, useState } from "react";
import { getGenresByContentType } from "../../../API/HomeApi";
import "./Movie.css"

const Movie = () => {

    const [genre, setGenre] = useState(null);
    useEffect(() => {
        console.log(genre);
    }, [genre]);

    const [contents, setContents] = useState(null);
    useEffect(() => {
        console.log(contents);
    }, [contents]);

    useEffect(() => {
        const fetchDomain = async () => {
            try {
                setGenre(await getGenresByContentType('movie').then(res => res.data));
            } catch (error) {
                console.error('Error fetching home contents:', error);
            }
        };
        fetchDomain();
    }, []);

    return (
        <div className="movie-page container">
            <div className="movie-page-wrap">
                <div className="movie-page-tab-btn">
                    {genre?.map(item =>(
                        <button key={item.id}>{item.second}</button>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default Movie;