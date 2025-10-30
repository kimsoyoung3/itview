import React, { useEffect, useRef, useState } from "react";
import "./CustomSelect.css"

const CustomSelect = ({ value, onChange, options }) => {
    const [open, setOpen] = useState(false);
    const selectRef = useRef(null);

    /* 바깥 클릭 시 드롭다운 닫기 */
    useEffect(() => {
        const handleClickOutside = (event) => {
            if (selectRef.current && !selectRef.current.contains(event.target)) {
                setOpen(false);
            }
        };

        document.addEventListener("click", handleClickOutside);
        return () => {
            document.removeEventListener("click", handleClickOutside);
        };
    }, []);

    const currentLabel = options.find((opt) => opt.value === value)?.label || "선택";

    return (
        <div className="custom-select" ref={selectRef}>
            <div
                className="custom-select-trigger"
                onClick={() => setOpen((prev) => !prev)}>
                {currentLabel}
                <div>
                    <img src={`${process.env.PUBLIC_URL}/icon/down.svg`} alt=""/>
                </div>
            </div>

            {/*옵션 창*/}
            {open && (
                <ul className="custom-select-options">
                    {options.map((opt) => (
                        <li
                            key={opt.value}
                            className={value === opt.value ? "selected" : ""}
                            onClick={() => {
                                onChange(opt.value);
                                setOpen(false);
                            }}
                        >
                            {opt.label}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default CustomSelect;
