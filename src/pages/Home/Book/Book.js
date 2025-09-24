import React, { useEffect, useState } from "react";
import { getGenresByContentType } from "../../../API/HomeApi";

const Book = () => {

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
                setGenre(await getGenresByContentType('book').then(res => res.data));
            } catch (error) {
                console.error('Error fetching home contents:', error);
            }
        };
        fetchDomain();
    }, []);

    return (
        <div>
            Book
        </div>
    );
}

export default Book;