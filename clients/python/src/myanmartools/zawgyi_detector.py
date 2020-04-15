import numpy as np
from ._params import get_mapping, load_params

class ZawgyiDetector:
    '''
    An estimator that predicts Zawgyi using two Markov chains, one
    for Unicode text and the other for Zawgyi text.

    Attributes
    ----------
    mapping : dict
        A mapping of Myanmar Unicode characters to the corresponding indices
        in the parameter array.
    params : numpy.ndarray
        A parameter array containing log likelihood ratios of
        Unicode to Zawgyi.
    '''
    def __init__(self):
        self.mapping = get_mapping()
        self.params = load_params()

    def get_zawgyi_probability(self, string):
        '''
        Computes Zawgyi probability.

        Parameters
        ----------
        string : str
            String to predict Zawgyi on.

        Returns
        -------
        float
            Zawgyi probability between 0 and 1, or negative infinity
            if there is no Myanmar Unicode character.
        '''
        indices = [self.mapping.get(char, 0) for char in string]
        # include starting and ending state probabilities
        previous = np.array([0] + indices)
        current = np.array(indices + [0])
        # ignore 0-to-0 transitions
        mask = np.logical_or(previous != 0, current != 0)
        # return negative inifinity if there are only 0-to-0 transitions,
        # which happens when there is no Myanmar Unicode character
        if not mask.any():
            return -np.inf
        # Pz/(Pu+Pz) = exp(logPz)/(exp(logPu)+exp(logPz))
        #            = 1/(1+exp(logPu-logPz))
        return 1.0 / (1.0 + 
            np.exp(self.params[previous[mask], current[mask]].sum()))
