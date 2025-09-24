import React, { useEffect, useState } from "react";
import { getGenresByContentType } from "../../../API/HomeApi";

const Movie = () => {

    const [domain, setDomain] = useState(null);
    useEffect(() => {
        console.log(domain);
    }, [domain]);

    const [contents, setContents] = useState(null);
    useEffect(() => {
        console.log(contents);
    }, [contents]);

    useEffect(() => {
        const fetchContents = async () => {
            try {
                setDomain(await getGenresByContentType('movie').then(res => res.data));
            } catch (error) {
                console.error('Error fetching home contents:', error);
            }
        };
        fetchContents();
    }, []);

    return (
        <div>
            Movie
        </div>
    );
}

export default Movie;