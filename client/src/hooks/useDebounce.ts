import React, {useCallback, useRef} from "react";

export const useDebouncedCallback = (
    callback: Function,
    delay: number,
    dependencies?: any[]
) => {
    const timeout = React.useRef<any>();

    // Avoid error about spreading non-iterable (undefined)
    const comboDeps = dependencies
        ? [callback, delay, ...dependencies]
        : [callback, delay];

    return React.useCallback((...args: any) => {
        if (timeout.current) {
            clearTimeout(timeout.current);
        }

        timeout.current = setTimeout(() => {
            callback(...args);
        }, delay);
    }, comboDeps);
};
export function useThrottle(callback: (...args: unknown[]) => void, delay = 20) {
    const isThrottled = useRef(false)
    return useCallback(
        (...args: any) => {
            if (isThrottled.current) return
            callback(args)
            isThrottled.current = true
            setTimeout(() => (isThrottled.current = false), delay)
        },
        [callback, delay]
    )
}

