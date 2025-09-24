import React, { useEffect, useState } from "react";

const Movie = () => {

    const [contents, setContents] = useState(null);
    useEffect(() => {
        console.log(contents);
    }, [contents]);

    useEffect(() => {
        const fetchContents = async () => {
            try {
                // let data = await getHomeContents('MOVIE', 1).then(response => response.data);
                // setContents(data);
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