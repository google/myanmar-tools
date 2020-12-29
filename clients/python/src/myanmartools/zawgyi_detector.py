"""Zawgyi detector module."""

from bisect import bisect_left
from importlib.resources import open_binary
from itertools import chain, filterfalse
from math import exp, inf, isnan, nan
from typing import Iterator, Optional

from ._params import check_signature, read_params


class ZawgyiDetector:
    """A detector of Myanmar Zawgyi encoding."""

    __slots__ = ['_chars', '_params']

    def __init__(self) -> None:
        """Intialize the detector."""
        with open_binary(
            'myanmartools.resources',
            'zawgyiUnicodeModel.dat'
        ) as stream:
            self._chars = check_signature(stream)
            self._params = read_params(stream)
            # the 0 node is for foreign characters so mark as nan
            self._params[0] = nan

    def _state(self, char: Optional[str]) -> int:
        """
        Return the state of a character.

        Return 0 for foreign characters.
        """
        if char is None:
            return 0
        i = bisect_left(self._chars, char)
        if i < len(self._chars) and self._chars[i] == char:
            return i + 1
        return 0

    def _llrs(self, string: str) -> Iterator[float]:
        """
        Return the log-likelihood ratios of consecutive character pairs.

        The first and last characters are paired with None on the left
        and right respectively.
        """
        size = len(self._chars) + 1
        return map(
            lambda i, j: self._params[self._state(i) * size + self._state(j)],
            chain((None,), string),
            chain(string, (None,))
        )

    def get_zawgyi_probability(self, string: str) -> float:
        """
        Return the Zawgyi probability of a string.

        Return negative infinity if there are only foreign characters.
        """
        if all(map(isnan, self._llrs(string))):
            return -inf
        total = sum(filterfalse(isnan, self._llrs(string)))
        # Pz/(Pu+Pz) = exp(lnPz)/(exp(lnPu)+exp(lnPz)) = 1/(1+exp(lnPu-lnPz))
        # prevent overflow when positive
        if total >= 0:
            z = exp(-total)
            return z / (z + 1)
        return 1 / (1 + exp(total))
